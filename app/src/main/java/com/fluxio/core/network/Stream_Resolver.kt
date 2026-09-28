package com.fluxio.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

object StreamResolver {

    private val STREAM_REGEX = Pattern.compile(
        """(?i)((?:https?:)?//[^\s"'<>]+?(?:\.m3u8|\.mpd|\.mp4|\.ts)(?:\?[^\s"'<>]*)?)"""
    )

    private const val DEFAULT_USER_AGENT = "IPTVSmarters/3.1.51 (Linux; Android 12) VLC/3.0.18 LibVLC/3.0.18"

    suspend fun resolveFinalStreamUrl(originalUrl: String, timeoutMs: Int = 4000): String = withContext(Dispatchers.IO) {
        val trimmed = originalUrl.trim()
        if (trimmed.isEmpty()) return@withContext ""

        // Si l'URL se termine deja par un fichier stream direct sans redirection
        val lower = trimmed.lowercase()
        if (lower.endsWith(".m3u8") || lower.endsWith(".ts") || lower.endsWith(".mpd") || lower.endsWith(".mp4")) {
            return@withContext trimmed
        }

        var currentUrl = trimmed
        var redirects = 0
        val maxRedirects = 4

        try {
            while (redirects < maxRedirects) {
                val urlObj = URL(currentUrl)
                val conn = (urlObj.openConnection() as HttpURLConnection).apply {
                    connectTimeout = timeoutMs
                    readTimeout = timeoutMs
                    instanceFollowRedirects = false
                    requestMethod = "GET"
                    setRequestProperty("User-Agent", DEFAULT_USER_AGENT)
                    setRequestProperty("Accept", "*/*")
                }

                val status = conn.responseCode
                if (status in 300..399) {
                    val location = conn.getHeaderField("Location")
                    conn.disconnect()
                    if (!location.isNullOrEmpty()) {
                        currentUrl = if (location.startsWith("http://") || location.startsWith("https://")) {
                            location
                        } else {
                            URL(urlObj, location).toString()
                        }
                        redirects++
                    } else {
                        break
                    }
                } else if (status in 200..299) {
                    val contentType = conn.contentType ?: ""
                    // Si le contenu est un manifest ou du texte HTML pouvant contenir le flux
                    if (!contentType.contains("video", ignoreCase = true) &&
                        !contentType.contains("mpegurl", ignoreCase = true) &&
                        !contentType.contains("octet-stream", ignoreCase = true)) {
                        
                        val body = conn.inputStream.bufferedReader().use { it.readText() }
                        conn.disconnect()

                        val matcher = STREAM_REGEX.matcher(body)
                        if (matcher.find()) {
                            return@withContext matcher.group(1) ?: currentUrl
                        }
                    } else {
                        conn.disconnect()
                    }
                    break
                } else {
                    conn.disconnect()
                    break
                }
            }
        } catch (_: Exception) {
            // En cas d'erreur de resolution, retourner l'URL originale
            return@withContext trimmed
        }

        currentUrl
    }
}

package com.fluxio.features.sports

import com.fluxio.shared.models.LiveChannel
import com.fluxio.shared.models.SportLeague
import com.fluxio.shared.models.SportMatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SportsDataRepository {

    suspend fun getTodayMatches(availableChannels: List<LiveChannel>): List<SportLeague> = withContext(Dispatchers.IO) {
        val sdfTime = SimpleDateFormat("HH:mm", Locale.FRANCE)
        val now = Date()
        val currentTimeStr = sdfTime.format(now)

        // Trouver les correspondances de chaînes disponibles
        fun findChannel(names: List<String>): LiveChannel? {
            return availableChannels.firstOrNull { ch ->
                val lower = ch.name.lowercase()
                names.any { lower.contains(it.lowercase()) }
            }
        }

        val canalPlus = findChannel(listOf("canal+", "canal plus"))
        val beIn1 = findChannel(listOf("bein sports 1", "bein 1", "bein sport 1"))
        val beIn2 = findChannel(listOf("bein sports 2", "bein 2", "bein sport 2"))
        val rmcSport = findChannel(listOf("rmc sport", "rmc"))
        val lEquipe = findChannel(listOf("l'equipe", "l'équipe", "equipe"))
        val tf1 = findChannel(listOf("tf1"))
        val france2 = findChannel(listOf("france 2"))
        val m6 = findChannel(listOf("m6"))

        val mockLeagues = listOf(
            SportLeague(
                name = "Ligue 1 McDonald's",
                logoUrl = "https://media.themoviedb.org/t/p/w500/pqUTCleNUiTLAVlelGxUgWn1ELh.png",
                matches = listOf(
                    SportMatch(
                        id = "l1_1",
                        homeTeam = "Paris Saint-Germain",
                        awayTeam = "Olympique de Marseille",
                        homeLogo = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?q=80&w=200&auto=format&fit=crop",
                        awayLogo = "https://images.unsplash.com/photo-1522778119026-d647f0596c20?q=80&w=200&auto=format&fit=crop",
                        league = "Ligue 1",
                        time = "20:45",
                        status = "Ce soir",
                        channelName = canalPlus?.name ?: "Canal+ Sport",
                        channelUrl = canalPlus?.url ?: "",
                        channelId = canalPlus?.id ?: ""
                    ),
                    SportMatch(
                        id = "l1_2",
                        homeTeam = "Olympique Lyonnais",
                        awayTeam = "AS Monaco",
                        homeLogo = "https://images.unsplash.com/photo-1574629810360-7efbbe195018?q=80&w=200&auto=format&fit=crop",
                        awayLogo = "https://images.unsplash.com/photo-1518091043644-c1d4457512c6?q=80&w=200&auto=format&fit=crop",
                        league = "Ligue 1",
                        time = "17:00",
                        status = "A venir",
                        channelName = beIn1?.name ?: "beIN SPORTS 1",
                        channelUrl = beIn1?.url ?: "",
                        channelId = beIn1?.id ?: ""
                    )
                )
            ),
            SportLeague(
                name = "UEFA Champions League",
                logoUrl = "https://media.themoviedb.org/t/p/w500/cm111bsDVlYaC1foL0itvEI4yLG.png",
                matches = listOf(
                    SportMatch(
                        id = "ucl_1",
                        homeTeam = "Real Madrid",
                        awayTeam = "Manchester City",
                        homeLogo = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?q=80&w=200&auto=format&fit=crop",
                        awayLogo = "https://images.unsplash.com/photo-1560272564-c83b66b1ad12?q=80&w=200&auto=format&fit=crop",
                        league = "Champions League",
                        time = "21:00",
                        status = "Ce soir",
                        channelName = canalPlus?.name ?: "Canal+",
                        channelUrl = canalPlus?.url ?: "",
                        channelId = canalPlus?.id ?: ""
                    ),
                    SportMatch(
                        id = "ucl_2",
                        homeTeam = "Bayern Munich",
                        awayTeam = "Arsenal FC",
                        homeLogo = "https://images.unsplash.com/photo-1517466787929-bc90951d0974?q=80&w=200&auto=format&fit=crop",
                        awayLogo = "https://images.unsplash.com/photo-1543351611-58f69d7c1781?q=80&w=200&auto=format&fit=crop",
                        league = "Champions League",
                        time = "21:00",
                        status = "Ce soir",
                        channelName = beIn2?.name ?: "beIN SPORTS 2",
                        channelUrl = beIn2?.url ?: "",
                        channelId = beIn2?.id ?: ""
                    )
                )
            ),
            SportLeague(
                name = "Omnisports & Événements",
                logoUrl = "https://media.themoviedb.org/t/p/w500/2uy2ZWcplrSObIyt4x0Y9rkG6qO.png",
                matches = listOf(
                    SportMatch(
                        id = "omni_1",
                        homeTeam = "Tournoi des 6 Nations",
                        awayTeam = "France vs Angleterre",
                        homeLogo = "https://images.unsplash.com/photo-1517649763962-0c623066013b?q=80&w=200&auto=format&fit=crop",
                        awayLogo = "https://images.unsplash.com/photo-1517649763962-0c623066013b?q=80&w=200&auto=format&fit=crop",
                        league = "Rugby International",
                        time = "16:00",
                        status = "A venir",
                        channelName = france2?.name ?: "France 2",
                        channelUrl = france2?.url ?: "",
                        channelId = france2?.id ?: ""
                    ),
                    SportMatch(
                        id = "omni_2",
                        homeTeam = "Grand Prix de Formule 1",
                        awayTeam = "Qualifications",
                        homeLogo = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?q=80&w=200&auto=format&fit=crop",
                        awayLogo = "https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?q=80&w=200&auto=format&fit=crop",
                        league = "Formule 1",
                        time = "15:00",
                        status = "Aujourd'hui",
                        channelName = canalPlus?.name ?: "Canal+ Sport",
                        channelUrl = canalPlus?.url ?: "",
                        channelId = canalPlus?.id ?: ""
                    ),
                    SportMatch(
                        id = "omni_3",
                        homeTeam = "L'Équipe du Soir",
                        awayTeam = "Débats en direct",
                        homeLogo = "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?q=80&w=200&auto=format&fit=crop",
                        awayLogo = "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?q=80&w=200&auto=format&fit=crop",
                        league = "Talk Sport",
                        time = "22:30",
                        status = "Ce soir",
                        channelName = lEquipe?.name ?: "La Chaîne L'Équipe",
                        channelUrl = lEquipe?.url ?: "",
                        channelId = lEquipe?.id ?: ""
                    )
                )
            )
        )

        mockLeagues
    }
}

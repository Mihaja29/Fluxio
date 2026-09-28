package com.fluxio.shared.models

data class SportMatch(
    val id: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeLogo: String = "",
    val awayLogo: String = "",
    val league: String,
    val leagueLogo: String = "",
    val time: String,
    val status: String = "A venir",
    val channelName: String,
    val channelUrl: String = "",
    val channelId: String = ""
)

data class SportLeague(
    val name: String,
    val logoUrl: String = "",
    val matches: List<SportMatch>
)

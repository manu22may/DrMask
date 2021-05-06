package com.capgemini.drmask

data class PollutionDetails(
    val list: List<PollutionList>
    )


data class PollutionList (
    val main:AirQualityIndex,
    val components : PollutionComponents
)

data class PollutionComponents(
        val co :Double,
        val no :Double,
        val pm2_5 :Double
)

data class AirQualityIndex(
    val aqi:Int
)


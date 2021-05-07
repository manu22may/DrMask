package com.capgemini.drmask.retrofitdatabase

data class CovidDetails(
        val data: Data
    )
data class Data(
        val summary: Summary,
        val regional :List<Regional>
)
data class Regional (
    val loc:String,
    val totalConfirmed:Long,
    val deaths:Long,
    val discharged:Long
)

data class Summary (
    val total :Long,
    val deaths:Long,
    val discharged:Long
)



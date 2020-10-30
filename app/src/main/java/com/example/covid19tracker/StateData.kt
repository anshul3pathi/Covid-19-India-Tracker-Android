package com.example.covid19tracker

data class StateData(
    val stateName: String,
    val confirmed: String,
    val active: String,
    val recovered: String,
    val deceased: String,
    val confirmedDelta: String,
    val activeDelta: String,
    val recoveredDelta: String,
    val deceasedDelta: String
)
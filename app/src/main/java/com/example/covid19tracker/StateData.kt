package com.example.covid19tracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "covid19_data_table")
data class StateData(
    @PrimaryKey(autoGenerate = false) val date: String,

    @ColumnInfo(name = "state_name") val stateName: String,

    @ColumnInfo(name = "confirmed") val confirmed: String,

    @ColumnInfo(name = "active") val active: String,

    @ColumnInfo(name = "recovered") val recovered: String,

    @ColumnInfo(name = "deceased") val deceased: String,

    @ColumnInfo(name = "confirmed_delta") val confirmedDelta: String,

    @ColumnInfo(name = "active_delta") val activeDelta: String,

    @ColumnInfo(name = "recovered_delta") val recoveredDelta: String,

    @ColumnInfo(name = "deceased_delta") val deceasedDelta: String
)
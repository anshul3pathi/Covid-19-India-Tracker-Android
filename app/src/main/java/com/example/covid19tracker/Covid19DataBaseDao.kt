package com.example.covid19tracker

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface Covid19DataBaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(state: StateData)
//
//    @Query("SELECT * FROM covid19_data_table WHERE state_name = :stateName LIMIT 1")
//    suspend fun checkForUpdatingTable(stateName: String)
//
//    @Query("DELETE FROM covid19_data_table WHERE date = :date")
//    suspend fun deleteOldTable(date: String)

    @Query("SELECT * FROM covid19_data_table order by state_name asc")
    fun getAllStates(): LiveData<List<StateData>>

}
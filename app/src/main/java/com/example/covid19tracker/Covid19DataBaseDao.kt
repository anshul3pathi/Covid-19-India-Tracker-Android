package com.example.covid19tracker

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface Covid19DataBaseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(state: StateData)
//
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT date FROM covid19_data_table WHERE date = :date")
    suspend fun checkForUpdatingTable(date: String): String?
//
    @Query("DELETE FROM covid19_data_table WHERE date = :date")
    suspend fun deleteOldTable(date: String)

    @Query("SELECT * FROM covid19_data_table order by state_name asc")
    fun getAllStates(): LiveData<List<StateData>>

}
package com.example.covid19tracker.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface Covid19DataBaseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(state: StateData)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(data: ArrayList<StateData>)

    @Query("DELETE FROM covid19_data_table")
    suspend fun deleteOldTable()

    @Query("SELECT * FROM covid19_data_table order by state_name asc")
    fun getAllStates(): LiveData<List<StateData>>

}
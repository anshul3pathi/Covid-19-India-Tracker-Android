package com.example.covid19tracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StateData::class], version = 1, exportSchema = false)
abstract class Covid19DataBase : RoomDatabase() {

    abstract val covidDataBaseDao: Covid19DataBaseDao

    companion object {
        @Volatile
        private var INSTANCE: Covid19DataBase? = null

        fun getDatabase(context: Context): Covid19DataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Covid19DataBase::class.java,
                    "covid19_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }

}
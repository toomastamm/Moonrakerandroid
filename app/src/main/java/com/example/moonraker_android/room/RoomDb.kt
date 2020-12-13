package com.example.moonraker_android.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moonraker_android.ui.sd_card.SDCardFileMetaDataResponse
import com.example.moonraker_android.ui.sd_card.SDCardFileResponse

@Database(entities = [SDCardFileResponse::class, SDCardFileMetaDataResponse::class], version = 1)
abstract class RoomDb : RoomDatabase() {
    companion object {
        lateinit var dbInstance: RoomDb

        @Synchronized // It will be created only once when multiple threads
        fun getInstance(applicationContext: Context) : RoomDb {
            if (!this::dbInstance.isInitialized) {
                dbInstance = Room.databaseBuilder(
                    applicationContext, RoomDb::class.java, "myRecipes")
                    .fallbackToDestructiveMigration() // each time schema changes, data is lost!
                    .allowMainThreadQueries() // if possible, use background thread instead
                    .build()
            }
            return dbInstance
        }
    }

    abstract fun getFileDao(): FileDao
}
package com.ake.ewhanoticeclient.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Board::class], version = 4, exportSchema = false)
abstract class BoardDatabase: RoomDatabase(){
    abstract val BoardDatabaseDao: BoardDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: BoardDatabase? = null

        fun getInstance(context: Context): BoardDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BoardDatabase::class.java,
                        "board_database")
                        .createFromAsset("boards.db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
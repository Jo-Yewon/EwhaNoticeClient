package com.ake.ewhanoticeclient.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BoardDatabaseDao{
    @Query("select * from board_table")
    fun getAllBoards(): List<DatabaseBoard>

    @Query("select * from board_table where board_id = :boardId")
    fun getBoard(boardId:Int): DatabaseBoard

    @Query("select * from board_table where title like :keyword")
    fun searchBoardByKeyword(keyword: String): List<DatabaseBoard>

    @Query("select distinct board_category from board_table")
    fun getAllTopics(): List<String>
}

@Database(entities = [DatabaseBoard::class], version = 5, exportSchema = false)
abstract class BoardDatabase: RoomDatabase(){
    abstract val boardDatabaseDao: BoardDatabaseDao

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
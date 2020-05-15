package com.ake.ewhanoticeclient.database

import android.content.Context
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface NoticeDatabaseDao{
    @Query("select * from notice_table where boardId == :boardId order by num desc")
    fun getNotices(boardId: Int): DataSource.Factory<Int, DatabaseNotice>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotices(notices:List<DatabaseNotice>)

    @Query("delete from notice_table where boardId == :boardId and num > :latest")
    fun deleteNotices(boardId: Int, latest: Int)
}

@Database(entities = [DatabaseNotice::class], version = 1, exportSchema = false)
abstract class NoticeDatabase: RoomDatabase(){
    abstract val noticeDatabaseDao: NoticeDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: NoticeDatabase? = null

        fun getInstance(context: Context): NoticeDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoticeDatabase::class.java,
                        "notice_database").build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
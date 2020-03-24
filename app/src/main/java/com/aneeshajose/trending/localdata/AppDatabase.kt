package com.aneeshajose.trending.localdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aneeshajose.trending.common.DB_NAME
import com.aneeshajose.trending.localdata.dao.RepositoryDao
import com.aneeshajose.trending.models.Repo

/**
 * Created by Aneesha Jose on 2020-03-24.
 * Main application room db
 */
@Database(entities = [Repo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRepositoryDao(): RepositoryDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, DB_NAME)
            .build()
    }
}
package com.treflor.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.treflor.data.db.dao.UserDao

@Database(
    entities = [UserDao::class],
    version = 1
)
abstract class TreflorDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var instance: TreflorDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            TreflorDatabase::class.java, "treflorEntries.db"
        )
            .build()
    }
}
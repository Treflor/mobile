package com.treflor.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.treflor.data.db.dao.*
import com.treflor.models.directionapi.DirectionApiResponse
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.models.Journey
import com.treflor.models.TrackedLocation
import com.treflor.models.User

@Database(
    entities = [ TrackedLocation::class, JourneyResponse::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TreflorDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun directionDao(): DirectionDao
    abstract fun trackedLocationsDao(): TrackedLocationsDao
    abstract fun journeyResponseDao(): JourneyResponseDao

    companion object {
        @Volatile
        private var instance: TreflorDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            TreflorDatabase::class.java,
            "treflor.db"
        )
            .build()
    }
}
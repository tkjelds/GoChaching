package dk.itu.moapd.gocaching.model.database

import dk.itu.moapd.gocaching.GeoCache
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [GeoCache :: class],version = 1)
@TypeConverters(GeoCacheTypeConverters::class)
abstract class GeoCacheDatabase : RoomDatabase() {

    abstract fun geoCacheDao(): GeoCacheDao


    companion object {
        // Use a singleton to prevent multiple instances of database
        // opening at the same time.
        @Volatile
        private var instance: GeoCacheDatabase? = null


        fun get(context: Context): GeoCacheDatabase {
            val checkInstance = instance
            if (checkInstance != null)
                return checkInstance
            synchronized(this) {
                val created = Room.databaseBuilder(
                    context.applicationContext,
                    GeoCacheDatabase::class.java, "geocache_database")
                    .build()
                instance = created
                return created
            }
        }
    }

}
package dk.itu.moapd.gocaching.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dk.itu.moapd.gocaching.GeoCache
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.UserCacheCrossRef
import dk.itu.moapd.gocaching.view.GeoCacheTypeConverters


@Database(entities = [GeoCache :: class,User ::class,UserCacheCrossRef::class],version = 7)
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
                    .fallbackToDestructiveMigration()
                    .build()
                instance = created
                return created
            }
        }
    }

}
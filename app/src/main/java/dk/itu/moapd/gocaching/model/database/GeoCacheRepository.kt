package dk.itu.moapd.gocaching.model.database

import dk.itu.moapd.gocaching.GeoCache
import android.app.Application
import androidx.lifecycle.LiveData
import java.util.*

class GeoCacheRepository (application: Application) {

    private val geoCacheDao: GeoCacheDao
    private val geoCaches: LiveData< List<GeoCache>>


    init {
        val db = GeoCacheDatabase.get(application)
        geoCacheDao = db.geoCacheDao()
        geoCaches = geoCacheDao.getGeoCaches()
    }

    suspend fun insert(geoCache: GeoCache) {
        geoCacheDao.insert(geoCache)
    }

    fun getGeoCaches() = geoCaches

    suspend fun update(geoCache: GeoCache){
        geoCacheDao.update(geoCache)
    }

    suspend fun delete(geoCache: GeoCache){
        geoCacheDao.delete(geoCache)
    }

    fun getGeoCacheByWhere(where:String):LiveData<GeoCache?>{
        return geoCacheDao.getGeoCacheByWhere(where)
    }

    fun getGeoCacheByWhere(id:UUID):LiveData<GeoCache?>{
        return geoCacheDao.getGeoCacheById(id)
    }



}
package dk.itu.moapd.gocaching.model.database

import dk.itu.moapd.gocaching.GeoCache
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

class GeoCacheViewModel(application: Application) : AndroidViewModel(application){

    private val geoCacheRepository: GeoCacheRepository = GeoCacheRepository(application)

    private val geoCaches: LiveData<List<GeoCache>>

    init {
        geoCaches = geoCacheRepository.getGeoCaches()
    }


    fun insert(geoCache: GeoCache) = viewModelScope.launch {
        geoCacheRepository.insert(geoCache)
    }

    fun delete(geoCache: GeoCache) = viewModelScope.launch {
        geoCacheRepository.delete(geoCache)
    }

    fun update(geoCache: GeoCache) = viewModelScope.launch {
        geoCacheRepository.update(geoCache)
    }

    fun geoGeoCacheByWhere(where:String):LiveData<GeoCache?>{
        return geoCacheRepository.getGeoCacheByWhere(where)
    }

    fun geoGeoCacheById(id:UUID):LiveData<GeoCache?>{
        return geoCacheRepository.getGeoCacheByWhere(id)
    }

    fun getGeoCaches(): LiveData<List<GeoCache>> {
        return geoCaches
    }


}
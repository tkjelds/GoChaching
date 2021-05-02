package dk.itu.moapd.gocaching.model.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.gocaching.*
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class GeoCacheViewModel(application: Application) : AndroidViewModel(application) {

    private val geoCacheRepository: GeoCacheRepository = GeoCacheRepository(application)

    private val geoCaches: LiveData<List<GeoCache>>
    private val users: LiveData<List<User>>
    private val cachesWithUser: LiveData<List<CachesWithUser>>
    private val userWithCaches: LiveData<List<UserWithCaches>>

    init {
        geoCaches = geoCacheRepository.getGeoCaches()
        users = geoCacheRepository.getUsers()
        cachesWithUser = geoCacheRepository.getCachesWithUsers()
        userWithCaches = geoCacheRepository.getUserWithCaches()
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

    fun insert(crossRef: UserCacheCrossRef) = viewModelScope.launch {
        geoCacheRepository.insert(crossRef)
    }

    fun updateGeoByCache(where_: String, cache_: String, updateDate_: Date) = viewModelScope.launch {
        geoCacheRepository.updateGeoByCache(where_, cache_, updateDate_)
    }

    fun updatePasswordByEmail(password_: String, email_: String) = viewModelScope.launch {
        geoCacheRepository.updatePasswordByEmail(password_, email_)
    }

    fun geoGeoCacheByWhere(where: String): LiveData<GeoCache?> {
        return geoCacheRepository.getGeoCacheByWhere(where)
    }

    fun geoGeoCacheById(id: UUID): LiveData<GeoCache?> {
        return geoCacheRepository.getGeoCacheByWhere(id)
    }

    fun getGeoCaches(): LiveData<List<GeoCache>> {
        return geoCaches
    }

    fun insert(user: User) = viewModelScope.launch {
        geoCacheRepository.insert(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        geoCacheRepository.delete(user)
    }

    fun update(user: User) = viewModelScope.launch {
        geoCacheRepository.update(user)
    }

    fun getUsers(): LiveData<List<User>> {
        return users
    }

    fun getUserWithCaches(): LiveData<List<UserWithCaches>> {
        return userWithCaches
    }

    fun getCachesWithUsers(): LiveData<List<CachesWithUser>> {
        return cachesWithUser
    }

    fun getPhotoFile(geoCache: GeoCache): File {
        return geoCacheRepository.getPhotoFile(geoCache)
    }

}
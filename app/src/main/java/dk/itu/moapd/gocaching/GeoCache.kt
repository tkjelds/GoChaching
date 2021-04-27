package dk.itu.moapd.gocaching

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class GeoCache(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var where: String = "",
    var cache: String = "",
    var lat: Double = 0.0,
    var long_:Double = 0.0,
    var date: Date = Date(),
    var updateDate: Date = Date()
)

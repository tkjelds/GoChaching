package dk.itu.moapd.gocaching

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class GeoCache(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var where: String = "",
    var cache: String = "",
    var date: Date = Date(),
    var updateDate: Date = Date()
)

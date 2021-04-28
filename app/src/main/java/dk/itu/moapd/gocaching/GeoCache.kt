package dk.itu.moapd.gocaching

import androidx.room.*
import dk.itu.moapd.gocaching.model.database.Difficulty
import dk.itu.moapd.gocaching.model.database.Difficulty.*
import java.util.*

@Entity
data class GeoCache(
    @PrimaryKey var gcid: UUID = UUID.randomUUID(),
    var where: String = "",
    var cache: String = "",
    var lat: Double = 0.0,
    var long_:Double = 0.0,
    var date: Date = Date(),
    var updateDate: Date = Date(),
    var difficulty: Difficulty = EASY
)

@Entity
data class User(
    @PrimaryKey var uid: UUID = UUID.randomUUID(),
    var name: String = "",
    var email: String = "",
    var isAdmin: Boolean = false
)

@Entity(primaryKeys = ["uid", "gcid"])
data class UserCacheCrossRef(
        val uid: UUID,
        val gcid: UUID
)

data class UserWithCaches(
        @Embedded val user: User,
        @Relation(
                parentColumn = "uid",
                entityColumn = "gcid",
                associateBy = Junction(UserCacheCrossRef::class)
        )
        val caches: List<GeoCache>
)

data class CachesWithUser(
        @Embedded val cache: GeoCache,
        @Relation(
                parentColumn = "gcid",
                entityColumn = "uid",
                associateBy = Junction(UserCacheCrossRef::class)
        )
        val Users: List<User>
)


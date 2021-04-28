package dk.itu.moapd.gocaching.view

import androidx.room.TypeConverter
import dk.itu.moapd.gocaching.model.database.Difficulty
import java.util.*

class GeoCacheTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date?{
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID?{
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?):String?{
        return uuid.toString()
    }

    @TypeConverter
    fun fromDifficulty(difficulty: Difficulty?):String?{
        return difficulty.toString()
    }
    @TypeConverter
    fun toDifficulty(difficulty: String?):Difficulty?{
        if (difficulty == "EASY")
            return Difficulty.EASY
        if (difficulty == "MEDIUM")
            return Difficulty.MEDIUM
        if (difficulty == "HARD")
            return Difficulty.HARD
        return null
    }
}
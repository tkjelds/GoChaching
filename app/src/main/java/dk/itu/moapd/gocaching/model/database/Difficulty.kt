package dk.itu.moapd.gocaching.model.database

import kotlinx.coroutines.yield

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD;

    override fun toString(): String {
        return super.toString()
    }

}

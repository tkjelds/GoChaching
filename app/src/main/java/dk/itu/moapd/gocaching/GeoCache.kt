package dk.itu.moapd.gocaching

import java.util.*

data class GeoCache(var cache: String, var where: String,var date:Date)

fun GeoCache.getDate() : Date {return this.date}

fun GeoCache.setDate(date_: Date){this.date = date_}

fun GeoCache.getCache() : String{ return this.cache }

fun GeoCache.getWhere() : String {return this.where}

fun GeoCache.setWhere(where_: String){this.where = where_}

fun GeoCache.setCache(cache_: String){this.cache = cache_}

fun GeoCache.toString() : String{return "$cache is placed at $where by $date"}
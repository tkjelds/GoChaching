package dk.itu.moapd.gocaching

data class GeoCache(var cache: String, var where: String)

fun GeoCache.getCache() : String{ return this.cache }

fun GeoCache.getWhere() : String {return this.where}

fun GeoCache.setWhere(where_: String){this.where = where_}

fun GeoCache.setCache(cache_: String){this.cache = cache_}

fun GeoCache.toString() : String{return "$cache is placed at $where"}
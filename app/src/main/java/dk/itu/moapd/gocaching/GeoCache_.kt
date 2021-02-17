package dk.itu.moapd.gocaching

class GeoCache_ {

        private var cache: String
        private var where: String

        constructor(cache: String, where: String){
            this.cache = cache
            this.where = where
        }

        fun getCache(): String{
            return cache
        }

        fun setCache(cache: String){
            this.cache = cache
        }
        fun getWhere(): String{
            return where
        }

        fun setWhere(where:String){
            this.where = where
        }

        override fun toString() : String{
            return "$cache is placed at $where"
        }
}
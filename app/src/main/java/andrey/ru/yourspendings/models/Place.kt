package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/4/19.
 */
data class Place(
    override var id:String="",
    var name:String="",
    var latitude:Double = 0.0,
    var longitude:Double = 0.0
): Model(id) {
    override fun toHashMap() =
        hashMapOf("id" to id,"name" to name, "latitude" to latitude, "longitude" to longitude)
    override fun getTitle() = this.name

    companion object {
        fun fromHashMap(data:Map<String,Any>):Place =
            Place(
                id = data["id"]?.toString() ?: "",
                name = data["name"]?.toString() ?: "",
                latitude = (data["latitude"]?.toString() ?: "0.0").toDouble(),
                longitude = (data["longitude"]?.toString() ?: "0.0").toDouble()
            )
        fun fromHashMapOfDB(data:Map<String,Any>) = fromHashMap(data)
    }
}

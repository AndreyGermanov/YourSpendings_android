package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/4/19.
 */
data class Place(
    var id:String,
    var name:String="",
    var latitude:Double = 0.0,
    var longitude:Double = 0.0
) {
    fun toHashMap():HashMap<String,Any> =
        hashMapOf("id" to id,"name" to name, "latitude" to latitude, "longitude" to longitude)

    companion object {
        fun fromHashMap(data:Map<String,Any>):Place =
            Place(
                id = data["id"]?.toString() ?: "",
                name = data["name"]?.toString() ?: "",
                latitude = (data["latitude"]?.toString() ?: "0.0").toDouble(),
                longitude = (data["longitude"]?.toString() ?: "0.0").toDouble()
            )
    }
}

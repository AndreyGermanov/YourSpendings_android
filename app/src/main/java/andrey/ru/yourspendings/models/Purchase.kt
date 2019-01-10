package andrey.ru.yourspendings.models

import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.collections.HashMap

/**
 * Created by Andrey Germanov on 1/9/19.
 */
class Purchase(override var id:String="",
               var date: LocalDateTime = LocalDateTime.now(),
               var place:Place = Place()):Model(id) {

    override fun getTitle():String {
        return formatDate()+" - "+place.name
    }

    private fun formatDate(inputDate:LocalDateTime?=null):String {
        val date = inputDate ?: this.date
        return date.toEpochSecond(ZoneOffset.UTC).toString()
    }

    override fun toHashMap(): HashMap<String, Any> {
        val result = super.toHashMap()
        result["place_id"] = place.id
        result["date"] = formatDate()
        return result
    }

    companion object {
        fun fromHashMap(data:Map<String,Any>):Purchase {
            return Purchase(
                id = data["id"]?.toString() ?: "",
                date = LocalDateTime.ofEpochSecond(
                    data["date"]?.toString()?.toLong() ?: LocalDateTime.now().toEpochSecond(
                    ZoneOffset.UTC),0, ZoneOffset.UTC),
                place = PlacesCollection.getItemById(data["place_id"]?.toString() ?: "") ?: Place()
            )
        }
        fun getClassName() = "Purchase"
    }
}
package andrey.ru.yourspendings.models

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
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
        return date.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"))
    }

    override fun toHashMap(): HashMap<String, Any> {
        val result = super.toHashMap()
        result["place_id"] = place.id
        result["date"] = date
        return result
    }

    override fun toHashMapForDB(): HashMap<String, Any> {
        val result = super.toHashMapForDB()
        result["date"] = Timestamp(date.toEpochSecond(ZoneOffset.UTC),0)
        return result
    }

    companion object {
        fun fromHashMap(data:Map<String,Any>):Purchase {
            return Purchase(
                id = data["id"]?.toString() ?: "",
                date = data["date"] as? LocalDateTime ?: LocalDateTime.now(),
                place = PlacesCollection.getItemById(data["place_id"]?.toString() ?: "") ?: Place()
            )
        }
        fun fromHashMapOfDB(data:Map<String,Any>):Purchase {
            return Purchase(
                id = data["id"]?.toString() ?: "",
                date = LocalDateTime.ofEpochSecond(((data["date"] as? Date)?.time ?: 0)/1000,0,ZoneOffset.UTC),
                place = PlacesCollection.getItemById(data["place_id"]?.toString() ?: "") ?: Place()
            )
        }
        fun getClassName() = "Purchase"
    }
}
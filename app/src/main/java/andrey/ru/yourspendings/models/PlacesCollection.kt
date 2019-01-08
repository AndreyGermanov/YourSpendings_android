package andrey.ru.yourspendings.models

import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Andrey Germanov on 1/4/19.
 */
object PlacesCollection: Collection<Place>() {

    override val tableName
        get() = "shops"

    override fun getCollectionName(): String = "shops"

    override fun newItem(data:Map<String,Any>):Place = Place.fromHashMap(data)

    fun validateItem(fields:HashMap<String,String>,callback:(result:Any) -> Unit) {
        val name = fields["name"] ?: ""
        val latitude = (fields["latitude"] ?: "").toDoubleOrNull()
        val longitude= (fields["longitude"] ?: "").toDoubleOrNull()
        if (name.isEmpty()) { callback("Name must be specified");return }
        if (latitude == null) { callback("Latitude must be specified"); return }
        if (longitude == null) { callback("Longitude must be specified"); return }

        val item = newItem(fields)
        if (item.id == "new") item.id = UUID.randomUUID().toString()
        if (items.find {it.id != item.id && it.name == item.name} != null) {
            callback("Item with specified name already exists")
            return
        }
        callback(item)
    }

}
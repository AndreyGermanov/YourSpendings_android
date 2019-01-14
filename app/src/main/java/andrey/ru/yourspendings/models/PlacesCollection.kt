package andrey.ru.yourspendings.models

import andrey.ru.yourspendings.extensions.distance
import android.annotation.SuppressLint
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Andrey Germanov on 1/4/19.
 */
@SuppressLint("StaticFieldLeak")
object PlacesCollection: Collection<Place>() {

    override val tableName
        get() = "shops"

    override fun getCollectionName(): String = "shops"

    override fun newItem(data:Map<String,Any>):Place = Place.fromHashMap(data)

    override fun newItemFromDB(data:Map<String,Any>):Place = Place.fromHashMapOfDB(data)

    override fun validateItem(fields:HashMap<String,Any>,callback:(result:Any) -> Unit) {
        val name = fields["name"]?.toString() ?: ""
        val latitude = (fields["latitude"]?.toString() ?: "").toDoubleOrNull()
        val longitude= (fields["longitude"]?.toString() ?: "").toDoubleOrNull()
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

    override fun deleteItem(id: String, callback: (error: String?) -> Unit) {
        val item = getItemById(id)!!
        PurchasesCollection.loadList {
            if (!PurchasesCollection.getList().any { it.place.id == item.id }) super.deleteItem(id, callback)
            else callback("This place used in purchases. Please remove purchases first")
        }
    }

    override fun getListTitle() = "Places List"

    fun getClosestPlace(lat:Double,lng:Double,callback:(place:Place?)->Unit) {
        loadList { callback(items.minBy { distance(it.latitude,it.longitude,lat,lng) })}
    }
}
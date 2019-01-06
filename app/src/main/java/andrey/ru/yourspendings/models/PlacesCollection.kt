package andrey.ru.yourspendings.models

import andrey.ru.yourspendings.db.DatabaseManager
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Andrey Germanov on 1/4/19.
 */
object PlacesCollection: IDatabaseSubscriber,IDataCollection {

    private const val tableName = "shops"
    private val items = ArrayList<Place>()
    private val db = DatabaseManager.getDB()
    private val subscribers = ArrayList<IDataSubscriber>()

    init { db.subscribe(this) }

    override fun onDataChange(changes: Map<String, List<Map<String, Any>>>) {
        for (key in changes.keys) {
            when (key) {
                "MODIFIED" -> modifyItems(changes[key]!!)
                "REMOVED" -> deleteItems(changes[key]!!)
                else -> addItems(changes[key]!!)
            }
        }
        subscribers.forEach { subscriber -> subscriber.onDataChange(items) }
    }

    private fun addItems(changes:List<Map<String,Any>>) = changes.forEach { items.add(newItem(it)) }

    private fun modifyItems(changes:List<Map<String,Any>>) {
        changes.forEach {change ->
            val index = items.indexOfFirst { it.id == change["id"]!!.toString() }
            if (index != -1) items[index] = newItem(change)
        }
    }

    private fun deleteItems(changes:List<Map<String,Any>>) {
        changes.forEach {change ->
            val index = items.indexOfFirst { it.id == change["id"]!!.toString() }
            if (index != -1) items.removeAt(index)
        }
    }

    override fun getCollectionName(): String = tableName

    private fun newItem(data:Map<String,Any>):Place = Place.fromHashMap(data)

    fun saveItem(fields:HashMap<String,String>,callback:(result:Any)->Unit) {
        validateItem(fields) { result ->
            when (result) {
                is String -> callback(result)
                is Place ->
                    db.saveItem(tableName,result.toHashMap()) { error ->
                        callback(error ?: result)
                    }
                else -> callback("System error")
            }
        }
    }

    fun deleteItem(id:String,callback:(error:String?)->Unit) {
        db.deleteItem(tableName,id) {error -> callback(error) }
    }

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

    override fun subscribe(subscriber: IDataSubscriber) {
        if (!subscribers.contains(subscriber)) subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: IDataSubscriber) {
        if (subscribers.contains(subscriber)) subscribers.remove(subscriber)
    }

}
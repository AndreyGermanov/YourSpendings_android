package andrey.ru.yourspendings.models

import andrey.ru.yourspendings.db.DatabaseManager

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

    private fun newItem(data:Map<String,Any>):Place {
        return Place(
            id = data["id"]?.toString() ?: "",
            name = data["name"]?.toString() ?: "",
            latitude = (data["latitude"]?.toString() ?: "0.0").toDouble(),
            longitude = (data["longitude"]?.toString() ?: "0.0").toDouble()
        )
    }

    override fun subscribe(subscriber: IDataSubscriber) {
        if (!subscribers.contains(subscriber)) subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: IDataSubscriber) {
        if (subscribers.contains(subscriber)) subscribers.remove(subscriber)
    }

}
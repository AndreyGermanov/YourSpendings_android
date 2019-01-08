package andrey.ru.yourspendings.models

import andrey.ru.yourspendings.db.DatabaseManager
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.services.IAuthServiceSubscriber
import java.util.ArrayList

/**
 * Created by Andrey Germanov on 1/8/19.
 */
open class Collection<T:Model>:IDataCollection<T>,IDatabaseSubscriber,IAuthServiceSubscriber {

    val items = ArrayList<T>()
    val db = DatabaseManager.getDB()
    val subscribers = ArrayList<IDataSubscriber<T>>()

    open val tableName
        get() = "entities"

    init {
        AuthManager.subscribe(this)
        db.subscribe(this)
    }

    override fun getCollectionName(): String = tableName

    override fun subscribe(subscriber: IDataSubscriber<T>) {
        if (!this.subscribers.contains(subscriber)) this.subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: IDataSubscriber<T>) {
        if (this.subscribers.contains(subscriber)) this.subscribers.remove(subscriber)
    }

    override fun onAuthStatusChanged(isAuthenticated: Boolean) {
        if (isAuthenticated) this.db.subscribe(this); else {
            this.items.clear()
            this.db.unsubscribe(this)
        }
    }

    override fun onDataChange(changes: Map<String, List<Map<String, Any>>>) {
        for (key in changes.keys) {
            when (key) {
                "MODIFIED" -> this.modifyItems(changes[key]!!)
                "REMOVED" -> this.deleteItems(changes[key]!!)
                else -> this.addItems(changes[key]!!)
            }
        }
        subscribers.forEach { subscriber -> subscriber.onDataChange(items) }
    }

    private fun addItems(changes:List<Map<String,Any>>) = changes.forEach { items.add(this.newItem(it)) }

    private fun modifyItems(changes:List<Map<String,Any>>) {
        changes.forEach {change ->
            val index = items.indexOfFirst { it.id == change["id"]!!.toString() }
            if (index != -1) items[index] = this.newItem(change)
        }
    }

    private fun deleteItems(changes:List<Map<String,Any>>) {
        changes.forEach {change ->
            val index = items.indexOfFirst { it.id == change["id"]!!.toString() }
            if (index != -1) items.removeAt(index)
        }
    }

    override fun saveItem(fields:HashMap<String,String>,callback:(result:Any)->Unit) {
        PlacesCollection.validateItem(fields) { result ->
            when (result) {
                is String -> callback(result)
                is Place ->
                    PlacesCollection.db.saveItem(PlacesCollection.tableName, result.toHashMap()) { error ->
                        callback(error ?: result)
                    }
                else -> callback("System error")
            }
        }
    }

    override fun deleteItem(id:String,callback:(error:String?)->Unit) {
        PlacesCollection.db.deleteItem(PlacesCollection.tableName,id) { error -> callback(error) }
    }

    open fun newItem(data:Map<String,Any>):T = Model.fromHashMap(data) as T

}
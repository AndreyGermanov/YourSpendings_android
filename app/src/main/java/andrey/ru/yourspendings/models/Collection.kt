package andrey.ru.yourspendings.models

import andrey.ru.yourspendings.db.DatabaseManager
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.services.IAuthServiceSubscriber
import java.util.ArrayList

/**
 * Created by Andrey Germanov on 1/8/19.
 */
@Suppress("UNCHECKED_CAST", "LeakingThis")
abstract class Collection<T:Model>:IDataCollection<T>,IDatabaseSubscriber,IAuthServiceSubscriber {

    protected val items = ArrayList<T>()
    private val db = DatabaseManager.getDB()
    private val subscribers = ArrayList<IDataSubscriber>()
    lateinit var rootPath:String

    open val tableName
        get() = "entities"

    init {
        AuthManager.subscribe(this)
        db.subscribe(this)
    }

    override fun getCollectionName(): String = tableName

    override fun subscribe(subscriber: IDataSubscriber) {
        if (!this.subscribers.contains(subscriber)) this.subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: IDataSubscriber) {
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
                "MODIFIED" -> modifyItems(changes[key]!!)
                "REMOVED" -> deleteItems(changes[key]!!)
                else -> addItems(changes[key]!!)
            }
        }
        subscribers.forEach { subscriber -> subscriber.onDataChange(items as ArrayList<Model>) }
    }

    private fun addItems(changes:List<Map<String,Any>>) = changes.forEach { change ->
        val index = items.indexOfFirst { it.id == change["id"]!!.toString() }
        if (index == -1) items.add(this.newItemFromDB(change))
    }

    private fun modifyItems(changes:List<Map<String,Any>>) {
        changes.forEach {change ->
            val index = items.indexOfFirst { it.id == change["id"]!!.toString() }
            if (index != -1) items[index] = newItemFromDB(change)
        }
    }

    private fun deleteItems(changes:List<Map<String,Any>>) {
        changes.forEach {change ->
            val index = items.indexOfFirst { it.id == change["id"]!!.toString() }
            if (index != -1) items.removeAt(index)
        }
    }

    override fun loadList(callback:(()->Unit)?) = db.getList(tableName) {addItems(it); if (callback!=null) callback()}

    override fun saveItem(fields:MutableMap<String,Any>,callback:(result:Any)->Unit) {
        validateItem(fields) { result ->
            when (result) {
                is String -> callback(result)
                is Model -> db.saveItem(tableName, result.toHashMapForDB()) { error -> callback(error ?: result)}
                else -> callback("System error")
            }
        }
    }

    override fun deleteItem(id:String,callback:(error:String?)->Unit) {
        db.deleteItem(tableName,id) { error -> callback(error) }
    }

    override fun newItem(data:Map<String,Any>):T = Model.fromHashMap(data) as T

    override fun newItemFromDB(data:Map<String,Any>):T = Model.fromHashMapOfDB(data) as T

    override fun getList() = items

    override fun getItemById(id:String):T? = items.find { it.id == id }

    fun clear() = items.clear()

    override fun setPath(rootPath:String) { this.rootPath = rootPath }

}
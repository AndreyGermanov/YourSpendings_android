package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.*
import androidx.lifecycle.MutableLiveData
import com.google.gson.internal.LinkedTreeMap

/**
 * Created by Andrey Germanov on 1/8/19.
 */
@Suppress("UNCHECKED_CAST")
open class EntityViewModel<T:Model>(open val Collection:IDataCollection<T>): PersistedViewModel(), IDataSubscriber<T> {

    private var mItems:List<T> = ArrayList<T>()
    val itemsObserver: MutableLiveData<List<T>> = MutableLiveData()
    var items:List<T>
        get() = mItems
        set(value) {
            mItems = value
            itemsObserver.postValue(value)
        }

    private var mCurrentItemId = ""
    val currentItemIdObserver: MutableLiveData<String> = MutableLiveData()
    var currentItemId:String
        get() = mCurrentItemId
        set(value) {
            mCurrentItemId = value
            currentItemIdObserver.postValue(value)
            save()
        }

    private var mScreenMode:ScreenMode = ScreenMode.LIST
    val screenModeObserver: MutableLiveData<ScreenMode> = MutableLiveData()
    var screenMode:ScreenMode
        get() = mScreenMode
        set(value) {
            mScreenMode = value
            screenModeObserver.postValue(value)
            save()
        }

    private var mIsLandscape:Boolean = false
    val isLandscapeObserver: MutableLiveData<Boolean> = MutableLiveData()
    var isLandscape:Boolean
        get() = mIsLandscape
        set(value) {
            mIsLandscape = value
            isLandscapeObserver.postValue(value)
            save()
        }


    private var mFields: Map<String,Any> = HashMap()
    var fields:Map<String,Any>
        get() = mFields
        set(value) {
            mFields = HashMap<String,Any>().apply{ putAll(value) }
            save()
        }

    var selectMode:Boolean? = null
    var isLoaded = false

    override fun initialize(rootPath:String) {
        Collection.setPath(rootPath)
        Collection.subscribe(this)
        items = Collection.getList()
        super.initialize(rootPath)
    }

    override fun onDataChange(items: ArrayList<T>) { this.items = items }

    fun getCurrentItem(): T? {
        if (currentItemId.isNotEmpty() && items.isNotEmpty()) return items.find { it.id == currentItemId }
        return null
    }

    fun saveChanges(fields:HashMap<String,Any>,callback:(error:String?)->Unit) {
        Collection.saveItem(fields) { result ->
            if (result is Model) currentItemId = result.id
            callback(result as? String ?: "Item saved successfully")
        }
    }

    fun deleteItem(callback:(error:String?)->Unit) {
        if (currentItemId.isEmpty()) return
        Collection.deleteItem(currentItemId) { error -> callback(error)}
    }

    fun clearFields() { fields = Collection.newItem(HashMap()).toHashMap() }

    fun getListTitle() = Collection.getListTitle()

    companion object {
        fun <T : Model> getViewModel(className: String, selectMode: Boolean): EntityViewModel<T>? = (when (className) {
            "Place" -> {
                val id = PlacesViewModel().apply {this.selectMode = selectMode}.getId()
                if (instances.containsKey(id)) instances[id]
                else {
                    instances[id] = PlacesViewModel().apply { this.selectMode = selectMode }; instances[id]
                }
            }
            "Purchase" -> {
                val id = PurchasesViewModel().apply {this.selectMode = selectMode}.getId()
                if (instances.containsKey(id)) instances[id]
                else {
                    instances[id] = PurchasesViewModel().apply { this.selectMode = selectMode }; instances[id]
                }
            }
            else -> null
        }) as? EntityViewModel<T>
        private val instances = HashMap<String, Any>()
    }

    override fun getId():String {
        var result = super.getId()
        if (this.selectMode == true) result += "_select"
        return result
    }

    override fun getState():HashMap<String,Any> = hashMapOf(
        "currentItemId" to mCurrentItemId,
        "fields" to HashMap<String,Any>().apply {putAll(mFields)},
        "screenMode" to mScreenMode,
        "isLandscape" to mIsLandscape
    )

    override fun setState(state: HashMap<String, Any>) {
        mCurrentItemId = state["currentItemId"]?.toString() ?: ""
        mFields = ((state["fields"] as? LinkedTreeMap<String, Any> ?: LinkedTreeMap()).toMap() as? HashMap<String,Any> ?: HashMap())
        mScreenMode = ScreenMode.valueOf(state["screenMode"]?.toString() ?: "LIST")
        mIsLandscape = state["isLandscape"]?.toString()?.toBoolean() ?: false
    }
}

enum class ScreenMode { LIST,ITEM }
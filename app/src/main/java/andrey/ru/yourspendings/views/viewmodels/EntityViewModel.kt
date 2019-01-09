package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * Created by Andrey Germanov on 1/8/19.
 */
open class EntityViewModel<T:Model>(open val Collection:IDataCollection<T> = Collection()): ViewModel(), IDataSubscriber<T> {

    private val items: MutableLiveData<List<T>> = MutableLiveData()
    private val currentItemId: MutableLiveData<String> = MutableLiveData()
    private val screenMode: MutableLiveData<ScreenMode> = MutableLiveData()
    private val isLandscape: MutableLiveData<Boolean> = MutableLiveData()
    private var fields: Map<String,String> = HashMap()

    init {
        screenMode.postValue(ScreenMode.LIST)
        isLandscape.postValue(false)
    }

    fun initialize() {
        Collection.subscribe(this)
    }

    override fun onDataChange(items: ArrayList<T>) = this.items.postValue(items)

    fun getItems() = items

    fun getCurrentItemId() = currentItemId

    fun setCurrentItemId(id:String) { currentItemId.postValue(id);}

    fun getScreenMode() = screenMode

    fun setScreenMode(mode: ScreenMode) { screenMode.postValue(mode);}

    fun getCurrentItem(): T? {
        val id = currentItemId.value
        val items = this.items.value
        if (id != null && items != null) return items.find { it.id == id }
        return null
    }

    fun isLandscapeMode():Boolean = isLandscape.value ?: false

    fun getLandscape() = isLandscape

    fun setLandscape(mode:Boolean) = isLandscape.postValue(mode)

    fun saveChanges(fields:HashMap<String,String>,callback:(error:String?)->Unit) {
        PlacesCollection.saveItem(fields) { result ->
            if (result is Place) currentItemId.postValue(result.id)
            callback(result as? String ?: "Item saved successfully")
        }
    }

    fun deleteItem(callback:(error:String?)->Unit) {
        val id = currentItemId.value ?: return
        Collection.deleteItem(id) { error -> callback(error)}
    }

    fun getFields() = fields

    fun setFields(fields:Map<String,String>) { this.fields = fields }

    fun clearFields() = setFields(mapOf("name" to "","latitude" to "0.0","longitude" to "0.0"))

    companion object {
        fun <T:Model> getViewModel(fragment: Fragment, className:String):EntityViewModel<T>? {
            var modelClass = PlacesViewModel::class.java
            when (className) {
                "Place" -> modelClass = PlacesViewModel::class.java
            }
            return fragment.activity?.run { ViewModelProviders.of(fragment.activity!!).get(modelClass) } as? EntityViewModel<T> ?:
            throw Exception("Invalid Activity")
        }
    }

}

enum class ScreenMode { LIST,ITEM }
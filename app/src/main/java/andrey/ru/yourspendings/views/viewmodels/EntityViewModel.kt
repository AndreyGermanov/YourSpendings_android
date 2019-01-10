package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * Created by Andrey Germanov on 1/8/19.
 */
@Suppress("UNCHECKED_CAST")
open class EntityViewModel<T:Model>(open val Collection:IDataCollection<T>): ViewModel(), IDataSubscriber<T> {

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
        items.postValue(Collection.getList())
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
        Collection.saveItem(fields) { result ->
            if (result is Model) currentItemId.postValue(result.id)
            callback(result as? String ?: "Item saved successfully")
        }
    }

    fun deleteItem(callback:(error:String?)->Unit) {
        val id = currentItemId.value ?: return
        Collection.deleteItem(id) { error -> callback(error)}
    }

    fun getFields() = fields

    fun setFields(fields:Map<String,String>) { this.fields = fields }

    fun clearFields() = setFields(Collection.newItem(HashMap()).toHashMap() as Map<String,String>)

    fun getListTitle() = Collection.getListTitle()

    companion object {
        fun <T:Model> getViewModel(activity: FragmentActivity, className:String):EntityViewModel<T>? = (when (className) {
            "Place" -> activity.run { ViewModelProviders.of(activity).get(PlacesViewModel::class.java) }
            "Purchase" -> activity.run { ViewModelProviders.of(activity).get(PurchasesViewModel::class.java) }
            else -> null
        }) as? EntityViewModel<T>
    }

}

enum class ScreenMode { LIST,ITEM }
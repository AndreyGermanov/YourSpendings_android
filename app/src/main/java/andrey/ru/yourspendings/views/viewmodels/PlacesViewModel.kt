package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.IDataSubscriber
import andrey.ru.yourspendings.models.Place
import andrey.ru.yourspendings.models.PlacesCollection
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Andrey Germanov on 1/5/19.
 */
class PlacesViewModel: ViewModel(),IDataSubscriber {

    private val places: MutableLiveData<List<Place>> = MutableLiveData()
    private val currentPlaceId: MutableLiveData<String> = MutableLiveData()
    private val screenMode: MutableLiveData<PlacesScreenMode> = MutableLiveData()
    private val isLandscape: MutableLiveData<Boolean> = MutableLiveData()
    private var fields: Map<String,String> = HashMap()

    init {
        PlacesCollection.subscribe(this)
        screenMode.postValue(PlacesScreenMode.LIST)
        isLandscape.postValue(false)
    }

    override fun onDataChange(items: ArrayList<Place>) = places.postValue(items)

    fun getPlaces() = places

    fun getCurrentPlaceId() = currentPlaceId

    fun setCurrentPlaceId(id:String) = currentPlaceId.postValue(id)

    fun getPlacesScreenMode() = screenMode

    fun setPlacesScreenMode(mode: PlacesScreenMode) = screenMode.postValue(mode)

    fun getCurrentPlace():Place? {
        val id = currentPlaceId.value
        val places = this.places.value
        if (id != null && places != null) return places.find { it.id == id }
        return null
    }

    fun isLandscapeMode():Boolean = isLandscape.value ?: false

    fun getLandscape() = isLandscape

    fun setLandscape(mode:Boolean) = isLandscape.postValue(mode)

    fun saveChanges(fields:HashMap<String,String>,callback:(error:String?)->Unit) {
        PlacesCollection.saveItem(fields) {result ->
            if (result is Place) currentPlaceId.postValue(result.id)
            callback(result as? String ?: "Item saved successfully")
        }
    }

    fun deleteItem(callback:(error:String?)->Unit) {
        val id = currentPlaceId.value ?: return
        PlacesCollection.deleteItem(id) { error -> callback(error)}
    }

    fun getFields() = fields

    fun setFields(fields:Map<String,String>) { this.fields = fields }
    
    fun clearFields() = setFields(mapOf("name" to "","latitude" to "0.0","longitude" to "0.0"))
}

enum class PlacesScreenMode { LIST,ITEM }
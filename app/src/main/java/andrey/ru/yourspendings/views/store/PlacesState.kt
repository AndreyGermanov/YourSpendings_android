package andrey.ru.yourspendings.views.store

import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.models.Place
import andrey.ru.yourspendings.models.PlacesCollection

@Suppress("UNCHECKED_CAST")
class PlacesState(override val state:AppState,override val modelName:String="Places"):ModelState(state,modelName) {

    override val items:ArrayList<Model>
        get() = PlacesCollection.getList().apply { sortBy { it.name }} as ArrayList<Model>

    var name:String
        get() = getValueFromMap("fields","name")?.toString() ?: ""
        set(value) { setValueToMap("fields","name",value)}

    var latitude:Double
        get() = getValueFromMap("fields","latitude")?.toString()?.toDoubleOrNull() ?: 0.0
        set(value) { setValueToMap("fields","latitude",value)}

    var longitude:Double
        get() = getValueFromMap("fields","longitude")?.toString()?.toDoubleOrNull() ?: 0.0
        set(value) { setValueToMap("fields","longitude",value) }

    override val item: Place?
        get() = PlacesCollection.getItemById(currentItemId)

    override fun getModelState(state:AppState):ModelState? {
        return if (modelName == "Places") state.placesState else state.selectPlaceState
    }

    override fun fillFieldsFromItem() {
        val item = item
        if (item != null) {
            name = item.name
            latitude = item.latitude
            longitude = item.longitude
        } else {
            name = ""
            latitude = 0.0
            longitude = 0.0
        }
    }

    override fun getListTitle() = "Places list"

}
package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.models.IDataSubscriber
import andrey.ru.yourspendings.models.PlacesCollection
import andrey.ru.yourspendings.services.LocationManager
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.ModelItemComponent
import andrey.ru.yourspendings.views.components.PlaceComponent
import andrey.ru.yourspendings.views.store.ModelState
import andrey.ru.yourspendings.views.store.PlacesState
import andrey.ru.yourspendings.views.utils.OnFieldChange

/**
 * Created by Andrey Germanov on 1/21/19.
 */
class PlaceContainer:ModelContainer(),IDataSubscriber {

    override lateinit var view: ModelItemComponent
    override lateinit var state: ModelState

    override fun initialize(context: MainActivity,modelState:ModelState?) {
        state = modelState ?: context.store.state.placesState
        view = PlaceComponent(context)
        component = view
        super.initialize(context)
    }

    override fun initComponent() {
        super.initComponent()
        view.mapFrame.addView(addChild(Container.getInstance(context,MapContainer::class.java,"placesMap")).setView())
    }

    override fun subscribeToDB() {
        PlacesCollection.subscribe(this)
    }

    override fun loadData(callback:()->Unit) {
        PlacesCollection.loadList { callback() }
    }

    override fun updateForm() {
        val state = state as PlacesState
        val view = view as PlaceComponent
        view.name.setText(state.name)
        view.latitude.setText(state.latitude.toString())
        view.longitude.setText(state.longitude.toString())
    }

    override fun setListeners() {
        super.setListeners()
        val state = state as PlacesState
        val view = view as PlaceComponent
        with (view) {
            name.addTextChangedListener(OnFieldChange { state.name = it })
            latitude.addTextChangedListener(OnFieldChange { state.latitude = it.toDoubleOrNull() ?: 0.0 })
            longitude.addTextChangedListener(OnFieldChange { state.longitude = it.toDoubleOrNull() ?: 0.0 })
            detectLatitudeButton.setOnClickListener {
                LocationManager.getLocation { lat, _ -> latitude.setText(lat.toString()) }
            }
            detectLongitudeButton.setOnClickListener {
                LocationManager.getLocation { _, lng -> longitude.setText(lng.toString()) }
            }
        }
    }

    override fun save(callback:(result:Any)->Unit) {
        PlacesCollection.saveItem(state.fields.apply{put("id",state.currentItemId)}) { result -> callback(result) }
    }

    override fun delete(callback:(error:String?)->Unit) {
        PlacesCollection.deleteItem(state.currentItemId) { error -> callback(error) }
    }

    override fun initNewItem(state:ModelState) {
        super.initNewItem(state)
        val view = view as PlaceComponent
        LocationManager.getLocation { latitude, longitude ->
            view.latitude.setText(latitude.toString())
            view.longitude.setText(longitude.toString())
        }
    }
}
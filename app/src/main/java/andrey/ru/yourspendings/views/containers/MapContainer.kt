package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.MapComponent
import andrey.ru.yourspendings.views.store.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created by Andrey Germanov on 1/31/19.
 */
class MapContainer:Container(),OnMapReadyCallback {

    lateinit var state: BaseState
    lateinit var view: MapComponent
    lateinit var map: GoogleMap
    private var marker: Marker? = null

    override fun initialize(context: MainActivity) {
        super.initialize(context)
        view = MapComponent(context)
        component = view
        initComponent()
    }

    fun initComponent() {
        view.render()
        updatePosition()
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0 ?: return
        if (marker != null) marker!!.remove()
        marker = map.addMarker(MarkerOptions().apply { position(LatLng(0.0,0.0))})
        setPosition()
    }

    private fun updatePosition() {
        if (view.mapView != null) { view.mapView!!.invalidate(); view.mapView!!.getMapAsync(this)}
    }

    private fun setPosition() {
        if (marker == null) marker = map.addMarker(MarkerOptions().apply { position(LatLng(0.0,0.0))})
        val state = context.store.state
        var position = LatLng(0.0,0.0)
        when (parent) {
            is PlaceContainer -> position = LatLng(state.placesState.latitude,state.placesState.longitude)
            is PurchaseContainer -> {
                if (state.purchasesState.place != null)
                    position = LatLng(state.purchasesState.place!!.latitude,state.purchasesState.place!!.longitude)
            }
        }
        marker!!.position = position
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position,14.0f))
    }

    override fun onStateChanged(state: AppState, prevState: AppState) {
        when(parent) {
            is PlaceContainer -> onPlaceStateChange(state.placesState,prevState.placesState)
            is PurchaseContainer -> onPurchasesStateChange(state.purchasesState,prevState.purchasesState)
        }
        onActivityStateChange(state.mainState,prevState.mainState)
        super.onStateChanged(state, prevState)
    }

    private fun onActivityStateChange(state:MainState, prevState:MainState) {
        if (state.screen != prevState.screen) {
            if (parent is PlaceContainer && state.screen == Screen.PLACES) updatePosition()
            if (parent is PurchaseContainer && state.screen == Screen.PURCHASES) updatePosition()
        }
        if (state.orientation != prevState.orientation) updatePosition()
        if (state.lifecycleState != prevState.lifecycleState && view.mapView != null) {
            when(state.lifecycleState) {
                LifecycleState.ON_CREATE -> view.mapView!!.onCreate(context.savedInstanceState)
                LifecycleState.ON_START -> view.mapView!!.onStart()
                LifecycleState.ON_STOP -> view.mapView!!.onStop()
                LifecycleState.ON_PAUSE -> view.mapView!!.onPause()
                LifecycleState.ON_RESUME -> view.mapView!!.onResume()
                LifecycleState.ON_LOW_MEMORY -> view.mapView!!.onLowMemory()
                LifecycleState.ON_DESTROY -> view.mapView!!.onDestroy()
                else -> {}
            }
            updatePosition()
        }

    }

    private fun onPlaceStateChange(state:PlacesState,prevState:PlacesState) {
        if (state.latitude != prevState.latitude || state.longitude != prevState.longitude ||
            state.mode != prevState.mode) updatePosition()
    }

    private fun onPurchasesStateChange(state: PurchasesState, prevState:PurchasesState) {
        if (state.place != prevState.place || state.mode != prevState.mode) updatePosition()
    }

}
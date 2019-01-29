package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.models.PlacesCollection
import andrey.ru.yourspendings.models.PurchasesCollection
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.services.CameraService
import andrey.ru.yourspendings.services.IAuthServiceSubscriber
import andrey.ru.yourspendings.services.LocationManager
import andrey.ru.yourspendings.views.containers.*
import andrey.ru.yourspendings.views.store.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders

@Suppress("PropertyName", "NAME_SHADOWING")
open class MainActivity : FragmentActivity(), IStoreSubscriber, IAuthServiceSubscriber {

    val REQUEST_TAKE_PICTURE_FROM_PICTURE_LIBRARY = 1

    lateinit var store: Store
    lateinit var container:MainContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocationManager.setup(this)
        CameraService.initialize(this)
        PlacesCollection.rootPath = this.filesDir.absolutePath
        PurchasesCollection.rootPath = this.filesDir.absolutePath
        store = ViewModelProviders.of(this).get(Store::class.java)
        store.rootView = this
        container = MainContainer()
        container.initialize(this)
        setContentView(container.setView())
        AuthManager.subscribe(this)
        with (store.state.mainState) {
            orientation = resources.configuration.orientation
            openDrawer = false
            lifecycleState = LifecycleState.ON_RESUME
        }
    }

    override fun onStateChanged(state: AppState, prevState:AppState) {
        container.onStateChanged(state,prevState)
    }

    override fun onAuthStatusChanged(isAuthenticated: Boolean) {
        if (!isAuthenticated) {
            store.state.mainState.screen = Screen.LOGIN
            PurchasesCollection.clear()
            PlacesCollection.clear()
        } else {
            if (store.state.mainState.screen == Screen.LOGIN) {
                store.state.mainState.screen = Screen.DASHBOARD
            }
        }
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int,data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) return
        when(requestCode) {
            REQUEST_TAKE_PICTURE_FROM_PICTURE_LIBRARY ->
                if (data != null && data.data != null) {
                    store.state.purchasesState.imageUri = data.data!!
                    store.state.purchasesState.imageCapturedFromLibrary = true
                }
        }
    }

    override fun onPause() {
        super.onPause()
        store.state.mainState.lifecycleState = LifecycleState.ON_PAUSE
    }

    override fun onResume() {
        super.onResume()
        store.state.mainState.lifecycleState = LifecycleState.ON_RESUME
    }

}

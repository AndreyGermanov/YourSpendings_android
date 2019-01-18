package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.services.IAuthServiceSubscriber
import andrey.ru.yourspendings.views.containers.Container
import andrey.ru.yourspendings.views.containers.DashboardContainer
import andrey.ru.yourspendings.views.containers.LoginContainer
import andrey.ru.yourspendings.views.fragments.dashboard.DashboardFragment
import andrey.ru.yourspendings.views.fragments.login.LoginContainerFragment
import andrey.ru.yourspendings.views.fragments.places.PlacesScreenFragment
import andrey.ru.yourspendings.views.fragments.purchases.PurchasesScreenFragment
import andrey.ru.yourspendings.views.fragments.ui.DialogFragmentListener
import andrey.ru.yourspendings.views.store.*
import andrey.ru.yourspendings.views.viewmodels.ActivityEvent
import andrey.ru.yourspendings.views.viewmodels.ActivityEventSubscriber
import andrey.ru.yourspendings.views.viewmodels.MainViewModel
import andrey.ru.yourspendings.views.viewmodels.Screens
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView

@Suppress("PropertyName", "NAME_SHADOWING")
open class MainActivity : FragmentActivity(),DialogFragmentListener,IStoreSubscriber, IAuthServiceSubscriber {


    lateinit var drawer:DrawerLayout
    private lateinit var navigationView:NavigationView
    lateinit var viewModel:MainViewModel
    val REQUEST_SELECT_ITEM = 1
    val REQUEST_TAKE_PICTURE_FROM_CAMERA = 2
    val REQUEST_PURCHASE_IMAGE_TO_REMOVE = 3
    val REQUEST_TAKE_PICTURE_FROM_PICTURE_LIBRARY = 4
    lateinit var store: Store
    lateinit var view:ViewGroup

    lateinit var dashboard: DashboardContainer
    lateinit var login: LoginContainer

    lateinit var children:ArrayList<Container>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = ViewModelProviders.of(this).get(Store::class.java)
        store.rootView = this
        setContentView(R.layout.activity_main)
        view = findViewById(R.id.screen_container)
        AuthManager.subscribe(this)
        loadContainers()
        render()
    }

    fun render() {
        setScreen(store.state.mainState.screen)
    }

    fun setScreen(screen: Screen) {
        view.removeAllViews()
        when (screen) {
            Screen.DASHBOARD -> view.addView(dashboard.getView())
            Screen.LOGIN -> view.addView(login.getView())
        }
    }

    fun loadContainers() {
        children = ArrayList()
        dashboard = Container.getInstance(this,DashboardContainer(this)).apply {
            this@MainActivity.children.add(this)
        }
        login = Container.getInstance(this,LoginContainer(this)).apply {
            this@MainActivity.children.add(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        view.removeAllViews()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStateChanged(state: AppState, prevState:AppState) {
        if (state.mainState.screen != prevState.mainState.screen) {
            setScreen(state.mainState.screen)
        }
        children.forEach { it.onStateChanged(state,prevState)}
    }

    override fun onAuthStatusChanged(isAuthenticated: Boolean) {
        if (!isAuthenticated) {
            store.state.mainState.screen = Screen.LOGIN
        } else {
            if (store.state.mainState.screen == Screen.LOGIN) {
                store.state.mainState.screen = Screen.DASHBOARD
            }
        }
    }

    open fun initActivity() {
        bindUI()
        setupListeners()
        setupScreen()
    }

    fun setViewModel() {
        viewModel = MainViewModel
        viewModel.initialize(this.filesDir.absolutePath)
        viewModel.onAuthStatusChanged(AuthManager.user != null)
    }

    open fun bindUI() {
        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
    }

    open fun setupListeners() {
        viewModel.screenObserver.observe(this, Observer {
            setupScreen(it)
        })

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> viewModel.screen = Screens.DASHBOARD
                R.id.nav_places -> viewModel.screen = Screens.PLACES
                R.id.nav_purchases -> viewModel.screen = Screens.PURCHASES
                R.id.nav_signout -> viewModel.logout()
            }
            true
        }
    }

    open fun setupScreen(screen: Screens? = null) {
        var screen = screen
        if (screen == null) screen = viewModel.screen
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        drawer.closeDrawer(GravityCompat.START)
        val transaction = supportFragmentManager.beginTransaction()
        when (screen) {
            Screens.DASHBOARD -> transaction.replace(R.id.screen_container, DashboardFragment())
            Screens.PLACES -> transaction.replace(R.id.screen_container, PlacesScreenFragment())
            Screens.PURCHASES -> transaction.replace(R.id.screen_container, PurchasesScreenFragment())
            Screens.NEW_PURCHASE -> transaction.replace(R.id.screen_container, PurchasesScreenFragment().apply {
                arguments = Bundle().apply { putBoolean("newItem",true);putString("currentItemId","new") }
            })
            else -> {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                transaction.replace(R.id.screen_container,
                    LoginContainerFragment()
                )
            }
        }
        transaction.commit()
    }

    fun subscribe(subscriber: ActivityEventSubscriber) = viewModel.subscribe(subscriber)

    fun unsubscribe(subscriber: ActivityEventSubscriber) = viewModel.unsubscribe(subscriber)

    override fun onPositiveButtonClicked(subscriberId:String,result: Any?) {
        viewModel.triggerEvent(ActivityEvent(subscriberId,"dialogSubmit",result))
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int,data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) return
        val subscriberId = data?.getStringExtra("subscriberId") ?: ""
        when(requestCode) {
            REQUEST_SELECT_ITEM ->
                if (data != null)
                    viewModel.triggerEvent(
                        ActivityEvent(subscriberId, "itemSelected", data.getStringExtra("selectedItemId"))
                    )
            REQUEST_TAKE_PICTURE_FROM_CAMERA ->
                viewModel.triggerEvent(
                    ActivityEvent(subscriberId, "purchaseImageCapturedFromCamera", null)
                )
            REQUEST_PURCHASE_IMAGE_TO_REMOVE ->
                if (data != null) {
                    viewModel.triggerEvent(
                        ActivityEvent(subscriberId, "purchaseImageRemoved", data.getStringExtra("imagePath"))
                    )
                }
            REQUEST_TAKE_PICTURE_FROM_PICTURE_LIBRARY ->
                if (data != null && data.data != null)
                    viewModel.triggerEvent(
                        ActivityEvent(subscriberId,"purchaseImageCapturedFromLibrary",data.data)
                    )

        }
    }

}

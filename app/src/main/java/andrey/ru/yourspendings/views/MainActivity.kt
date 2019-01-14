package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.fragments.dashboard.DashboardFragment
import andrey.ru.yourspendings.views.fragments.login.LoginContainerFragment
import andrey.ru.yourspendings.views.fragments.places.PlacesScreenFragment
import andrey.ru.yourspendings.views.fragments.purchases.PurchasesScreenFragment
import andrey.ru.yourspendings.views.fragments.ui.DialogFragmentListener
import andrey.ru.yourspendings.views.viewmodels.ActivityEvent
import andrey.ru.yourspendings.views.viewmodels.ActivityEventSubscriber
import andrey.ru.yourspendings.views.viewmodels.MainViewModel
import andrey.ru.yourspendings.views.viewmodels.Screens
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView

@Suppress("PropertyName")
open class MainActivity : AppCompatActivity(),DialogFragmentListener {

    lateinit var drawer:DrawerLayout
    private lateinit var navigationView:NavigationView
    lateinit var viewModel:MainViewModel
    val REQUEST_SELECT_ITEM = 1
    val REQUEST_TAKE_PICTURE_FROM_CAMERA = 2
    val REQUEST_PURCHASE_IMAGE_TO_REMOVE = 3
    val REQUEST_TAKE_PICTURE_FROM_PICTURE_LIBRARY = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewModel()
        bindUI()
        setupListeners()
        setupScreen(viewModel.getCurrentScreen())
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    open fun bindUI() {
        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
    }

    open fun setupListeners() {
        viewModel.getScreen().observe(this, Observer { setupScreen(it) })
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> viewModel.setScreen(Screens.DASHBOARD)
                R.id.nav_places -> viewModel.setScreen(Screens.PLACES)
                R.id.nav_purchases -> viewModel.setScreen(Screens.PURCHASES)
                R.id.nav_signout -> viewModel.logout()
            }
            true
        }
    }

    private fun setupScreen(screen: Screens) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        drawer.closeDrawer(GravityCompat.START)
        val transaction = supportFragmentManager.beginTransaction()
        when (screen) {
            Screens.DASHBOARD -> transaction.replace(R.id.screen_container, DashboardFragment())
            Screens.PLACES -> transaction.replace(R.id.screen_container, PlacesScreenFragment())
            Screens.PURCHASES -> transaction.replace(R.id.screen_container, PurchasesScreenFragment())
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
                if (data != null)
                    viewModel.triggerEvent(
                        ActivityEvent(subscriberId, "purchaseImageRemoved", data.getStringExtra("imagePath"))
                    )
            REQUEST_TAKE_PICTURE_FROM_PICTURE_LIBRARY ->
                if (data != null && data.data != null)
                    viewModel.triggerEvent(
                        ActivityEvent(subscriberId,"purchaseImageCapturedFromLibrary",data.data)
                    )

        }
    }
}

package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.MainComponent
import andrey.ru.yourspendings.views.store.AppState
import andrey.ru.yourspendings.views.store.MainState
import andrey.ru.yourspendings.views.store.ModelScreenMode
import andrey.ru.yourspendings.views.store.Screen
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

/**
 * Created by Andrey Germanov on 1/21/19.
 */
class MainContainer:Container() {

    lateinit var view: MainComponent

    override fun initialize(context: MainActivity) {
        super.initialize(context)
        view = MainComponent(context)
        component = view
        initComponent()
        setListeners()
    }

    private fun initComponent() {
        component.render()
        setupUI()
    }

    private fun setupUI() {
        children.clear()
        addChild(Container.getInstance(context,HeaderContainer::class.java)).setView(view.headerContainer)
        setupMainScreen()
    }

    private fun setupMainScreen() {
        view.screenContainer.removeAllViews()
        val ctx = this.context
        view.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        view.drawer.closeDrawer(GravityCompat.START)
        when (context.store.state.mainState.screen) {
            Screen.DASHBOARD -> addChild(Container.getInstance(ctx, DashboardContainer::class.java))
            Screen.PLACES -> addChild(Container.getInstance(ctx, PlacesScreenContainer::class.java))
            Screen.PURCHASES -> addChild(Container.getInstance(ctx,PurchasesScreenContainer::class.java))
            Screen.LOGIN -> {
                view.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                addChild(Container.getInstance(ctx, LoginContainer::class.java))
            }
        }.setView(view.screenContainer)
    }

    fun setListeners() {
        val store = context.store
        view.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> { store.state.mainState.screen = Screen.DASHBOARD }
                R.id.nav_places -> {
                    store.state.mainState.screen = Screen.PLACES
                    store.state.placesState.mode = ModelScreenMode.LIST
                }
                R.id.nav_purchases -> {
                    store.state.mainState.screen = Screen.PURCHASES
                    store.state.purchasesState.mode = ModelScreenMode.LIST
                }
                R.id.nav_signout -> { AuthManager.logout() }
            }
            true
        }
        view.drawer.addDrawerListener(OnDrawerListener(store.state.mainState))
    }

    override fun onStateChanged(state: AppState, prevState: AppState) {
        if (state.mainState.screen != prevState.mainState.screen) setupUI()
        if (state.mainState.openDrawer != prevState.mainState.openDrawer) {
            if (state.mainState.openDrawer) view.drawer.openDrawer(GravityCompat.START,true)
            if (!state.mainState.openDrawer) view.drawer.closeDrawer(GravityCompat.START,true)
        }
        super.onStateChanged(state, prevState)
    }
}

class OnDrawerListener(val state: MainState): DrawerLayout.DrawerListener {
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
    override fun onDrawerClosed(drawerView: View) { state.openDrawer = false }
    override fun onDrawerOpened(drawerView: View) { state.openDrawer = true }
    override fun onDrawerStateChanged(newState: Int) {}
}
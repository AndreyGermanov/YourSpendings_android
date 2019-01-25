package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.HeaderComponent
import andrey.ru.yourspendings.views.store.AppState
import andrey.ru.yourspendings.views.store.ModelScreenMode
import andrey.ru.yourspendings.views.store.Screen
import android.content.res.Configuration
import android.view.View

/**
 * Created by Andrey Germanov on 1/19/19.
 */
class HeaderContainer : Container() {

    lateinit var view:HeaderComponent

    override fun initialize(context:MainActivity) {
        super.initialize(context)
        view = HeaderComponent(context)
        component = view
        initComponent()
        setListeners()
    }

    private fun initComponent() {
        val ctx = context
        with (component as HeaderComponent) {
            screen = this@HeaderContainer.context.store.state.mainState.screen
            modelScreenMode = ModelScreenMode.ITEM
            if (screen == Screen.PURCHASES || screen == Screen.PLACES) {
                val modelState = ctx.store.state.getModelState()
                if (modelState != null) modelScreenMode = modelState.mode
            }
            render()
            setupUI()
        }
        setListeners()
    }

    private fun setupUI() {
        val mainState = context.store.state.mainState
        when (mainState.screen) {
            Screen.DASHBOARD -> setupDashboardHeader()
            Screen.PLACES,Screen.PURCHASES -> setupModelHeader()
            Screen.LOGIN -> view.visibility = View.GONE
        }
    }

    private fun setupDashboardHeader() {
        with(view) {
            menuButton.visibility = View.VISIBLE
            backButton.visibility = View.GONE
            addButton.visibility = View.GONE
            headerTitle.text = context.getString(R.string.dashboard)
            this.visibility = View.VISIBLE
        }
    }

    private fun setupModelHeader() {
        val mainState = context.store.state.mainState
        val modelState = context.store.state.getModelState()!!
        with (view) {
            menuButton.visibility = View.VISIBLE
            if (modelState.selectMode) menuButton.visibility = View.GONE
            if (modelState.mode == ModelScreenMode.ITEM) {
                headerTitle.text = modelState.item?.getTitle() ?: modelState.getListTitle()
                if (mainState.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    backButton.visibility = View.GONE
                    addButton.visibility = View.VISIBLE
                } else {
                    backButton.visibility = View.VISIBLE
                    addButton.visibility = View.GONE
                }
            } else {
                backButton.visibility = View.GONE
                addButton.visibility = View.VISIBLE
                headerTitle.text = modelState.getListTitle()
            }
            visibility = View.VISIBLE
        }
    }

    fun setListeners() {
        view.menuButton.setOnClickListener {
           context.store.state.mainState.openDrawer = true
        }
        view.addButton.setOnClickListener {
            context.store.state.getModelState()?.apply {
                currentItemId = "new"
                mode = ModelScreenMode.ITEM
            }
        }
        view.backButton.setOnClickListener {
            context.store.state.getModelState()?.apply { mode = ModelScreenMode.LIST}
        }
    }

    override fun onStateChanged(state: AppState, prevState: AppState) {
        if (state.mainState.screen != prevState.mainState.screen) setupUI()
        val modelState = state.getModelState()
        val prevModelState = state.getModelState(prevState)
        if (modelState != null && prevModelState != null) {
            if (modelState.mode != prevModelState.mode) setupUI()
        }
        if (state.mainState.orientation != prevState.mainState.orientation) setupUI()
        super.onStateChanged(state, prevState)
    }
}
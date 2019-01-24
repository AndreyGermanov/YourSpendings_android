package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.ModelScreenComponent
import andrey.ru.yourspendings.views.store.AppState
import andrey.ru.yourspendings.views.store.ModelScreenMode
import andrey.ru.yourspendings.views.store.ModelState
import android.content.res.Configuration
import android.view.View

/**
 * Created by Andrey Germanov on 1/19/19.
 */
@Suppress("UNCHECKED_CAST")
open class ModelScreenContainer:Container() {

    lateinit var modelState: ModelState
    lateinit var listContainerClass: Class<Container>
    lateinit var itemContainerClass: Class<Container>
    lateinit var view:ModelScreenComponent

    override fun initialize(context: MainActivity,modelState:ModelState?) {
        this.initialize(context)
    }

    override fun initialize(context:MainActivity) {
        super.initialize(context)
        view = ModelScreenComponent(context)
        component = view
        initComponent()
        setListeners()
    }

    private fun initComponent() {
        view.render()
        setupScreen()
    }

    private fun setupScreen() {
        val state = modelState
        children.clear()
        val listContainer = addChild(Container.getModelInstance(this.context,listContainerClass,modelState))
        val itemContainer = addChild(Container.getModelInstance(this.context,itemContainerClass,modelState))
        if (state.selectMode) {
            addChild(Container.getInstance(context,HeaderContainer::class.java,"SelectHeader")).setView(view.header)
            view.header.visibility = View.VISIBLE
        }
        when {
            context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> {
                listContainer.setView(view.list)
                itemContainer.setView(view.content)
            }
            modelState.mode == ModelScreenMode.LIST -> listContainer.setView(view.content)
            modelState.mode == ModelScreenMode.ITEM -> itemContainer.setView(view.content)
        }
    }

    fun setListeners() {}

    override fun onStateChanged(state: AppState, prevState: AppState) {
        if (state.mainState.orientation != prevState.mainState.orientation) {
            initComponent()
            setListeners()
        }
        if (modelState.mode != modelState.getModelState(prevState)?.mode) {
            initComponent()
            setListeners()
        }
        super.onStateChanged(state, prevState)
    }
}
package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.models.IDataSubscriber
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.models.PlacesCollection
import andrey.ru.yourspendings.models.PurchasesCollection
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.ModelListComponent
import andrey.ru.yourspendings.views.store.AppState
import andrey.ru.yourspendings.views.store.ModelScreenMode
import andrey.ru.yourspendings.views.store.ModelState

/**
 * Created by Andrey Germanov on 1/20/19.
 */
open class ModelListContainer:Container(),IDataSubscriber {

    var state: ModelState? = null
    lateinit var view: ModelListComponent

    override fun initialize(context: MainActivity,modelState:ModelState?) {
        this.initialize(context)
    }

    override fun initialize(context: MainActivity) {
        super.initialize(context)
        if (state == null) return
        view = ModelListComponent(context)
        component = view
        initComponent()
        setListeners()
        subscribeToDB()
    }

    open fun subscribeToDB() {}

    private fun initComponent() {
        view.state = state
        view.render()
        loadData {}
    }

    fun setListeners() {
        view.editButton.setOnClickListener {
            state!!.mode = ModelScreenMode.ITEM
        }
        view.selectButton.setOnClickListener {
            state!!.itemIsSelected = true
        }
    }

    private fun loadData(callback:()->Unit) {
        state!!.isLoading = true
        PlacesCollection.loadList {
            PurchasesCollection.loadList {
                state!!.isLoading = false
                callback()
            }
        }
    }

    override fun onStateChanged(state: AppState, prevState: AppState) {
        val newState = this.state?.getModelState(state) ?: return
        val newIsLoading = newState.isLoading
        val oldState = this.state?.getModelState(prevState) ?: newState
        val oldIsLoading = this.state?.getModelState(prevState)?.isLoading ?: false
        val component = component as ModelListComponent
        component.state = newState
        if (newIsLoading != oldIsLoading) {
            with(component) {
                updateUI()
                modelListAdapter.notifyDataSetChanged()
            }
        }
        if (newState.currentItemId != oldState.currentItemId) {
            component.modelListAdapter.notifyDataSetChanged()
        }
        super.onStateChanged(state, prevState)
    }

    override fun onDataChange(items: ArrayList<Model>) {}

}
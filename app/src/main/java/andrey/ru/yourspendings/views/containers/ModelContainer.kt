package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.IDataSubscriber
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.ModelItemComponent
import andrey.ru.yourspendings.views.store.*
import android.app.AlertDialog
import android.view.View
import android.widget.Toast

/**
 * Created by Andrey Germanov on 1/22/19.
 */
open class ModelContainer:Container(),IDataSubscriber {

    open lateinit var view: ModelItemComponent
    open lateinit var state: ModelState

    override fun initialize(context: MainActivity,modelState:ModelState?) {
        this.initialize(context)
    }

    override fun initialize(context: MainActivity) {
        super.initialize(context)
        initComponent()
        setListeners()
        subscribeToDB()
    }

    open fun initComponent() {
        view.render()
        if (state.itemsUpdateCounter == 0) loadData {}
        updateUI()
    }

    open fun loadData(callback:()->Unit) { callback() }

    open fun updateUI() {
        view.saveButton.visibility = View.VISIBLE
        view.deleteButton.visibility = if (state.currentItemId != "new") View.VISIBLE else View.GONE
        if (state.currentItemId.isEmpty()) {
            view.formContainer.visibility = View.GONE
        } else {
            view.formContainer.visibility = View.VISIBLE
            updateForm()
        }
    }

    open fun updateForm() { }

    open fun setListeners() {
        view.saveButton.setOnClickListener {
            save { result -> when(result) {
                is String -> state.popupMessage = result
                is Model -> {
                    state.popupMessage = context.getString(R.string.save_success)
                    state.currentItemId = result.id
                }
            }
            }
        }
        view.deleteButton.setOnClickListener {
            AlertDialog.Builder(this.context).apply {
                setMessage(context.getString(R.string.are_you_sure))
                setPositiveButton(context.getString(R.string.yes)) { _, _ -> delete { error ->
                    if (error!= null) {
                        state.popupMessage = error
                    } else {
                        state.currentItemId = ""
                        state.mode = ModelScreenMode.LIST
                    }
                } }
                setNegativeButton(context.getString(R.string.no)) { _, _ -> }
            }.create().show()
        }
    }

    open fun save(callback:(result:Any)->Unit) { callback(0)}

    open fun delete(callback:(result:String?)->Unit) { callback(null) }

    open fun subscribeToDB() {}

    override fun onStateChanged(state: AppState, prevState: AppState) {
        val oldModelState = this.state.getModelState(prevState)!!
        val newModelState = this.state.getModelState(state)!!
        onModelStateChanged(newModelState,oldModelState)
        super.onStateChanged(state, prevState)
    }

    open fun onModelStateChanged(state:ModelState,prevState:ModelState) {
        if (state.currentItemId != prevState.currentItemId) onSwitchItem(state)

        if (state.popupMessage != prevState.popupMessage && state.popupMessage.isNotEmpty()) {
            Toast.makeText(context, state.popupMessage, Toast.LENGTH_LONG).show()
            state.popupMessage = ""
        }
    }

    open fun onSwitchItem(state: ModelState) {
        when (state.currentItemId) {
            "new" -> initNewItem(state)
            else -> initExistingItem(state)
        }
        updateUI()
    }

    open fun initNewItem(state: ModelState) {
        state.fillFieldsFromItem()
    }

    open fun initExistingItem(state: ModelState) {
        state.fillFieldsFromItem()
    }

    override fun onDataChange(items: ArrayList<Model>) {
        state.itemsUpdateCounter += 1
    }

}
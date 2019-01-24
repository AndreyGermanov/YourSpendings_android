package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.models.PlacesCollection
import andrey.ru.yourspendings.models.PurchasesCollection
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.DashboardComponent
import andrey.ru.yourspendings.views.store.ModelScreenMode
import andrey.ru.yourspendings.views.store.Screen

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
class DashboardContainer:Container() {

    lateinit var view:DashboardComponent

    override fun initialize(context:MainActivity) {
        super.initialize(context)
        view = DashboardComponent(context)
        component = view
        initComponent()
        setListeners()
    }

    private fun initComponent() {
        view.render()
    }

    fun setListeners() {
        val state = context.store.state
        view.purchasesButton.setOnClickListener {
            state.mainState.screen = Screen.PURCHASES
            state.purchasesState.mode = ModelScreenMode.LIST
        }
        view.newPurchaseButton.setOnClickListener {
            state.purchasesState.mode = ModelScreenMode.ITEM
            state.purchasesState.currentItemId = "new"
            state.mainState.screen = Screen.PURCHASES
        }
        view.placesButton.setOnClickListener {
            state.mainState.screen = Screen.PLACES
            state.placesState.mode = ModelScreenMode.LIST
        }
        view.logoutButton.setOnClickListener {
            PurchasesCollection.clear()
            PlacesCollection.clear()
            AuthManager.logout()
        }


    }

}
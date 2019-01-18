package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.models.PlacesCollection
import andrey.ru.yourspendings.models.PurchasesCollection
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.Component
import andrey.ru.yourspendings.views.components.DashboardComponent
import android.widget.Button

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
class DashboardContainer(context: MainActivity):Container(context) {

    override var component: Component = DashboardComponent(context)
    private lateinit var purchasesBtn: Button

    init {
        initComponent()
        setListeners()
    }

    fun initComponent() {
        with(component as DashboardComponent) {
            render()
            bindUI()
        }
    }

    fun setListeners() {
        val state = context.store.state
        with(component as DashboardComponent) {
            purchasesButton.setOnClickListener {
            }
            logoutButton.setOnClickListener {
                PurchasesCollection.clear()
                PlacesCollection.clear()
                AuthManager.logout()
            }
        }

    }

    override fun getView():DashboardComponent {
        return component as DashboardComponent
    }

}
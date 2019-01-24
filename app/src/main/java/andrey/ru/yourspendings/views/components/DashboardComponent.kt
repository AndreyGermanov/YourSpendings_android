package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.widget.Button

/**
 * Created by Andrey Germanov on 1/17/19.
 */
class DashboardComponent(context: MainActivity): Component(context) {

    lateinit var purchasesButton: Button
    lateinit var placesButton: Button
    lateinit var newPurchaseButton: Button
    lateinit var logoutButton: Button

    override fun render() {
        addView(inflate(context,R.layout.fragment_dashboard,null))
        bindUI()
    }

    override fun bindUI() {
        purchasesButton = findViewById(R.id.purchases_list_button)
        placesButton = findViewById(R.id.places_list_button)
        newPurchaseButton = findViewById(R.id.new_purchase_button)
        logoutButton = findViewById(R.id.logout_button)
    }

}
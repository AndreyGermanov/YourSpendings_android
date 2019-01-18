package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.content.Context
import android.widget.Button

/**
 * Created by Andrey Germanov on 1/17/19.
 */
class DashboardComponent(context: MainActivity): Component(context) {

    lateinit var purchasesButton: Button
    lateinit var logoutButton: Button

    override fun render() {
        addView(inflate(context,R.layout.fragment_dashboard,null))
    }

    override fun bindUI() {
        purchasesButton = findViewById(R.id.purchases_list_button)
        logoutButton = findViewById(R.id.logout_button)
    }
}
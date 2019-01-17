package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.fragments.places.PlacesScreenFragment
import andrey.ru.yourspendings.views.fragments.purchases.PurchasesScreenFragment
import andrey.ru.yourspendings.views.viewmodels.Screens
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

/**
 * Created by Andrey Germanov on 1/11/19.
 */
class SelectModelActivity : MainActivity() {

    override fun setupScreen(screen: Screens?) {
        val model = intent.extras?.getString("model") ?: ""
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        val transaction = supportFragmentManager.beginTransaction()
        var fragment = Fragment()
        when (model) {
            "Place" -> fragment = PlacesScreenFragment()
            "Purchase" -> fragment = PurchasesScreenFragment()
        }
        val arguments = Bundle()
        arguments.putBoolean("selectMode",true)
        arguments.putString("currentItemId",intent.extras?.getString("currentItemId") ?: "")
        arguments.putString("subscriberId",intent.extras?.getString("subscriberId") ?: "")
        fragment.arguments = arguments
        transaction.replace(R.id.screen_container,fragment)
        transaction.commit()
    }
}
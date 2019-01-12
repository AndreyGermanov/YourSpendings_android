package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.fragments.places.PlacesScreenFragment
import andrey.ru.yourspendings.views.fragments.purchases.PurchasesScreenFragment
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by Andrey Germanov on 1/11/19.
 */
class SelectModelActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupScreen()
    }

    private fun setupScreen() {
        val model = intent.extras?.getString("model") ?: ""
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
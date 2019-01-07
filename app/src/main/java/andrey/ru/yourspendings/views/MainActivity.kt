package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.views.fragments.LoginContainerFragment
import andrey.ru.yourspendings.views.fragments.PlacesScreenFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupScreen()
    }

    fun setupScreen() {
        val transaction = supportFragmentManager.beginTransaction()
        if (AuthManager.isLogin) {
            transaction.replace(R.id.screen_container, PlacesScreenFragment())
        } else {
            transaction.replace(R.id.screen_container, LoginContainerFragment())
        }
        transaction.commit()
    }

}

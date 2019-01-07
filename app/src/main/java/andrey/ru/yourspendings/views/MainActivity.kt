package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.fragments.PlacesScreenFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.screen_container,
            PlacesScreenFragment()
        ).commit()
    }

}

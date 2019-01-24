package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

/**
 * Created by Andrey Germanov on 1/21/19.
 */
@SuppressLint("ViewConstructor")
class MainComponent(context: MainActivity):Component(context) {

    lateinit var drawer: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var headerContainer: FrameLayout
    lateinit var screenContainer: FrameLayout

    override fun render() {
        removeAllViews()
        addView(inflate(context, R.layout.activity_main,null))
        bindUI()
    }

    override fun bindUI() {
        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        screenContainer = findViewById(R.id.screen_container)
        headerContainer = findViewById(R.id.header_container)
    }

}
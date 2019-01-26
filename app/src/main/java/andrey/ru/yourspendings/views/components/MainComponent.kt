package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.LinearLayout
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
    private val drawerParams = DrawerLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)

    override fun render() {
        removeAllViews()
        addView(DrawerLayout(context).apply { layoutParams = drawerParams
            fitsSystemWindows = true
            addView(LinearLayout(context).apply { layoutParams = fullScreen()
                orientation = LinearLayout.VERTICAL
                addView(FrameLayout(context).apply { layoutParams = horizontal()
                    headerContainer = this
                })
                addView(FrameLayout(context).apply { layoutParams = fullScreen()
                    screenContainer = this
                })
            })
            addView(NavigationView(context).apply { layoutParams = drawerParams.apply { gravity = Gravity.START }
                fitsSystemWindows = true
                inflateMenu(R.menu.drawer_view)
                navigationView = this
            })
            drawer = this
        })
    }
}
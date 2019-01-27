package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.store.ModelScreenMode
import andrey.ru.yourspendings.views.store.Screen
import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by Andrey Germanov on 1/19/19.
 */
@SuppressLint("ViewConstructor")
class HeaderComponent(val context: MainActivity): Component(context) {

    lateinit var screen: Screen
    lateinit var modelScreenMode: ModelScreenMode

    lateinit var menuButton: ImageButton
    lateinit var backButton: ImageButton
    lateinit var addButton: ImageButton
    lateinit var headerTitle: TextView

    override fun render() {
        removeAllViews()
        addView(LinearLayout(context).apply { layoutParams = horizontal().apply { gravity = Gravity.CENTER }
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(context.resources.getColor(R.color.colorPrimary,context.theme))
            addView(headerButton(R.drawable.ic_menu_black_24dp,context.resources.getString(R.string.main_menu))
                .also { menuButton = it})
            addView(headerButton(R.drawable.ic_arrow_back_black_24dp,context.resources.getString(R.string.back))
                .also { backButton = it})
            addView(headerButton(R.drawable.ic_add_black_24dp,context.resources.getString(R.string.add))
                .also { addButton = it})
            addView(textView("").apply { setPadding(0,15,0,10)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setTextAppearance(R.style.headerText)
            }.also { headerTitle = it })
        })
    }

    private fun headerButton(resourceId:Int,title:String):ImageButton = imageButton(resourceId,title).apply {
        setPadding(0,10,0,10)
    }
}
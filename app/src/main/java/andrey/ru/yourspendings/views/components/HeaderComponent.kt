package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.store.ModelScreenMode
import andrey.ru.yourspendings.views.store.Screen
import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageButton
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
        addView(View.inflate(context, R.layout.fragment_model_header,null))
        bindUI()
    }

    override fun bindUI() {
        menuButton = findViewById(R.id.drawer_menu)
        backButton = findViewById(R.id.back_button)
        addButton = findViewById(R.id.add_button)
        headerTitle = findViewById(R.id.header_title)
    }


}
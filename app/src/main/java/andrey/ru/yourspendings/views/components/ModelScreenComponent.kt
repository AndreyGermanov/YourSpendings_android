package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.FrameLayout

/**
 * Created by Andrey Germanov on 1/19/19.
 */
@SuppressLint("ViewConstructor")
class ModelScreenComponent(val context:MainActivity) : Component(context) {

    lateinit var list: FrameLayout
    lateinit var content: FrameLayout
    lateinit var header:FrameLayout

    override fun render() {
        removeAllViews()
        when (context.resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> renderPortrait()
            Configuration.ORIENTATION_LANDSCAPE -> renderLandscape()
        }
        header = findViewById(R.id.model_header_container)
        bindUI()
    }

    private fun renderPortrait() {
        addView(inflate(context, R.layout.fragment_model_screen_portrait, null))
        content = findViewById(R.id.fragment_container)
    }

    private fun renderLandscape() {
        addView(inflate(context, R.layout.fragment_model_screen_landscape, null))
        list = findViewById(R.id.list_fragment)
        content = findViewById(R.id.item_fragment)
    }

    override fun bindUI() {}
}
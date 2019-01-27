package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout

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
        addView(LinearLayout(context).apply {
            layoutParams = fullScreen()
            orientation = LinearLayout.VERTICAL
            addView(FrameLayout(context).apply {
                layoutParams = horizontal()
                visibility = View.GONE
            }.also { header = it })
            addView(
                when (context.resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> renderPortrait()
                    else -> renderLandscape()
                }
            )
        })
    }

    private fun renderPortrait():View = FrameLayout(context).apply { layoutParams = fullScreen() }.also { content = it }

    private fun renderLandscape():View = LinearLayout(context).apply { layoutParams = fullScreen()
        orientation = LinearLayout.HORIZONTAL
        addView(FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT,MATCH_PARENT)
        }.also { list = it})
        addView(FrameLayout(context).apply { layoutParams = fullScreen()}.also { content = it })
    }

}
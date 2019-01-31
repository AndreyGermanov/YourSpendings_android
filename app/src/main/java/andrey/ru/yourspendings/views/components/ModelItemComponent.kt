package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TableLayout

/**
 * Created by Andrey Germanov on 1/21/19.
 */
@SuppressLint("ViewConstructor")
open class ModelItemComponent(open val context:MainActivity):Component(context) {

    lateinit var saveButton: Button
    lateinit var deleteButton: Button
    lateinit var formContainer: ViewGroup
    lateinit var buttonsContainer: LinearLayout
    lateinit var mapFrame: FrameLayout

    override fun render() = addView(renderLayout())

    open fun renderLayout():LinearLayout = LinearLayout(context).apply {
        layoutParams = fullScreen()
        setPadding(10,10,10,0)
        orientation = LinearLayout.VERTICAL
        addView(renderForm())
        addView(formButtons().also { buttonsContainer = it })
        addView(FrameLayout(context).apply { layoutParams = fullScreen()}.also { mapFrame = it})
    }.also { formContainer = it }

    open fun renderForm():TableLayout = TableLayout(context).apply {
        layoutParams = tableLayoutParams
        visibility = View.VISIBLE
        setColumnStretchable(1, true)
    }

    private fun formButtons() = LinearLayout(context).apply {
        layoutParams = horizontal()
        addView(button(context.getString(R.string.save)).apply { layoutParams = wrap()}.also { saveButton = it })
        addView(button(context.getString(R.string.delete)).apply { layoutParams = wrap()}.also { deleteButton = it })
    }

    fun detectButton() = imageButton(R.drawable.ic_location_searching_black_24dp,"").apply {
        layoutParams = rowLayoutParams
    }

    fun selectButton() = imageButton(R.drawable.ic_border_color_black_24dp,"").apply {
        layoutParams = rowLayoutParams
    }

}
package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.widget.EditText
import android.widget.ImageButton

/**
 * Created by Andrey Germanov on 1/21/19.
 */
@SuppressLint("ViewConstructor")
class PlaceComponent(override val context:MainActivity): ModelItemComponent(context) {

    lateinit var name: EditText
    lateinit var latitude: EditText
    lateinit var longitude: EditText
    lateinit var detectLatitudeButton: ImageButton
    lateinit var detectLongitudeButton: ImageButton

    override fun renderForm() = super.renderForm().apply {
        addView(renderTextRow(context.resources.getString(R.string.place_name)) { name = it })
        addView(renderNumericRow(context.resources.getString(R.string.latitude)) { latitude = it }.apply {
            addView(detectButton().also { detectLatitudeButton = it })
        })
        addView(renderNumericRow(context.resources.getString(R.string.longitude)) { longitude = it }.apply {
            addView(detectButton().also { detectLongitudeButton = it })
        })
    }
}
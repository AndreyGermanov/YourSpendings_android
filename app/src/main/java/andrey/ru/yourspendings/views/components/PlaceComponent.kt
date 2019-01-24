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

    override fun render() {
        addView(inflate(context,R.layout.fragment_place,null))
        bindUI()
    }

    override fun bindUI() {
        super.bindUI()
        name = findViewById(R.id.place_name)
        latitude = findViewById(R.id.place_latitude)
        longitude = findViewById(R.id.place_longitude)
        detectLatitudeButton = findViewById(R.id.place_latitude_button)
        detectLongitudeButton = findViewById(R.id.place_longitude_button)
    }

}
package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Place
import andrey.ru.yourspendings.services.LocationManager
import android.annotation.SuppressLint
import android.view.View
import android.widget.EditText
import android.widget.ImageButton

@SuppressLint("ValidFragment")

/**
 * Created by Andrey Germanov on 1/5/19.
 */
@Suppress("NAME_SHADOWING")
class PlaceFragment(override var fragmentId:Int = R.layout.fragment_place,
                    override var className:String = Place.getClassName())
    : EntityItemFragment<Place>(fragmentId,className) {

    private lateinit var name: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var latitudeButton: ImageButton
    private lateinit var longitudeButton: ImageButton

    override fun bindUI(view:View) {
        name = view.findViewById(R.id.place_name)
        latitude = view.findViewById(R.id.place_latitude)
        longitude = view.findViewById(R.id.place_longitude)
        latitudeButton = view.findViewById(R.id.place_latitude_button)
        longitudeButton = view.findViewById(R.id.place_longitude_button)
        super.bindUI(view)
    }

    override fun setListeners(view:View) {
        super.setListeners(view)

        name.setOnKeyListener(this)
        latitude.setOnKeyListener(this)
        longitude.setOnKeyListener(this)

        latitudeButton.setOnClickListener { LocationManager.getLocation { lat, _ -> setCoordinate(latitude,lat) }}
        longitudeButton.setOnClickListener { LocationManager.getLocation { _, lng -> setCoordinate(longitude, lng) }}
    }

    override fun getFields():HashMap<String,String> =
        hashMapOf("name" to name.text.toString().trim(),
            "latitude" to latitude.text.toString().trim(),
            "longitude" to longitude.text.toString().trim(),
            "id" to currentItemId.trim()
        )

    override fun setFields(fields:Map<String,Any>?) {
        val fields = fields ?: viewModel.getFields()
        name.setText(fields["name"]?.toString() ?: "")
        latitude.setText(fields["latitude"]?.toString() ?: "0.0")
        longitude.setText(fields["longitude"]?.toString() ?: "0.0")
    }

    private fun setCoordinate(field:EditText,value:Double) {
        field.setText(value.toString())
        viewModel.setFields(getFields())
    }
}
package andrey.ru.yourspendings.views.fragments.places

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Place
import andrey.ru.yourspendings.services.LocationManager
import andrey.ru.yourspendings.views.fragments.ModelItemFragment
import android.view.View
import android.widget.EditText
import android.widget.ImageButton

/**
 * Created by Andrey Germanov on 1/5/19.
 */
@Suppress("NAME_SHADOWING")
class PlaceFragment : ModelItemFragment<Place>() {

    override var fragmentId:Int = R.layout.fragment_place
    override var className:String = Place.getClassName()

    private lateinit var name: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var latitudeButton: ImageButton
    private lateinit var longitudeButton: ImageButton

    override fun bindUI(view:View) {
        with(view) {
            name = findViewById(R.id.place_name)
            latitude = findViewById(R.id.place_latitude)
            longitude = findViewById(R.id.place_longitude)
            latitudeButton = findViewById(R.id.place_latitude_button)
            longitudeButton = findViewById(R.id.place_longitude_button)
        }
        super.bindUI(view)
    }

    override fun setListeners(view:View) {
        super.setListeners(view)

        name.addTextChangedListener(this)
        latitude.addTextChangedListener(this)
        longitude.addTextChangedListener(this)

        latitudeButton.setOnClickListener { LocationManager.getLocation { lat, _ -> setCoordinate(latitude,lat) }}
        longitudeButton.setOnClickListener { LocationManager.getLocation { _, lng -> setCoordinate(longitude, lng) }}
    }

    override fun getFields():HashMap<String,Any> =
        hashMapOf("name" to name.text.toString().trim(),
            "latitude" to latitude.text.toString().trim(),
            "longitude" to longitude.text.toString().trim(),
            "id" to currentItemId.trim()
        )

    override fun setFields(fields:Map<String,Any>?) {
        var fields = fields ?: viewModel.fields
        if (fields.isEmpty()) fields = viewModel.fields
        name.setText(fields["name"]?.toString() ?: "")
        latitude.setText(fields["latitude"]?.toString() ?: "0.0")
        longitude.setText(fields["longitude"]?.toString() ?: "0.0")
    }

    private fun setCoordinate(field:EditText,value:Double) {
        field.setText(value.toString())
        viewModel.fields = getFields()
    }
}
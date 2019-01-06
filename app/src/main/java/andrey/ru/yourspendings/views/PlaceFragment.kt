package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.services.LocationManager
import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

/**
 * Created by Andrey Germanov on 1/5/19.
 */
open class PlaceFragment: Fragment(), View.OnKeyListener {

    protected var currentPlaceId = ""
    protected lateinit var viewModel:PlacesViewModel
    private lateinit var name: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private lateinit var latitudeButton: ImageButton
    private lateinit var longitudeButton: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setViewModel()
        val view = inflater.inflate(R.layout.fragment_place,container,false)
        view.visibility = View.INVISIBLE
        bindUI(view)
        setListeners(view)
        return view
    }

    open fun bindUI(view:View) {
        name = view.findViewById(R.id.place_name)
        latitude = view.findViewById(R.id.place_latitude)
        longitude = view.findViewById(R.id.place_longitude)
        saveButton = view.findViewById(R.id.save_place_btn)
        deleteButton = view.findViewById(R.id.delete_place_btn)
        latitudeButton = view.findViewById(R.id.place_latitude_button)
        longitudeButton = view.findViewById(R.id.place_longitude_button)
        setFields()
        LocationManager.setup(this.activity!!)
    }

    open fun setViewModel() {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(PlacesViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        currentPlaceId = viewModel.getCurrentPlaceId().value ?: ""
    }

    open fun setListeners(view:View) {
        viewModel.getCurrentPlaceId().observe(this, Observer<String> { id ->
            currentPlaceId = id
            prepareItemForm(view)
        })

        saveButton.setOnClickListener { saveItem() }

        deleteButton.setOnClickListener {
            AlertDialog.Builder(this.context).apply {
                setMessage("Are you sure ?")
                setPositiveButton(getString(R.string.yes)) { dialog, id -> deleteItem() }
                setNegativeButton(getString(R.string.no)) { dialog, id -> }
            }.create().show()
        }

        name.setOnKeyListener(this)
        latitude.setOnKeyListener(this)
        longitude.setOnKeyListener(this)

        latitudeButton.setOnClickListener { LocationManager.getLocation { lat, _ -> setCoordinate(latitude,lat) }}
        longitudeButton.setOnClickListener { LocationManager.getLocation { _, lng -> setCoordinate(longitude, lng) }}
    }

    private fun prepareItemForm(view:View) {
        val place = viewModel.getPlaces().value?.find {it.id == currentPlaceId }
        if (currentPlaceId.isNotEmpty()) view.visibility = View.VISIBLE; else view.visibility = View.INVISIBLE
        if (currentPlaceId == "new") deleteButton.visibility = View.GONE; else deleteButton.visibility = View.VISIBLE
        if (viewModel.getFields()["id"] != place?.id) {
            name.setText(place?.name ?: "")
            latitude.setText(place?.latitude?.toString() ?: "0.0")
            longitude.setText(place?.longitude?.toString() ?: "0.0")
            viewModel.setFields(getFields())
        }
    }

    private fun saveItem() {
        viewModel.saveChanges(getFields()) {error ->
            if (error != null) Toast.makeText(this.context,error,Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteItem() {
        viewModel.deleteItem { error ->
            if (error != null) Toast.makeText(this.context,error,Toast.LENGTH_LONG).show()
            viewModel.setCurrentPlaceId("")
            viewModel.setPlacesScreenMode(PlacesScreenMode.LIST)
        }

    }

    fun getFields():HashMap<String,String> =
        hashMapOf("name" to name.text.toString().trim(),
            "latitude" to latitude.text.toString().trim(),
            "longitude" to longitude.text.toString().trim(),
            "id" to currentPlaceId.trim()
        )

    fun setFields() {
        val fields = viewModel.getFields()
        name.setText(fields["name"] ?: "")
        latitude.setText(fields["latitude"] ?: "0.0")
        longitude.setText(fields["longitude"] ?: "0.0")
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        viewModel.setFields(getFields())
        return false
    }

    fun setCoordinate(field:EditText,value:Double) {
        field.setText(value.toString())
        viewModel.setFields(getFields())
    }

}
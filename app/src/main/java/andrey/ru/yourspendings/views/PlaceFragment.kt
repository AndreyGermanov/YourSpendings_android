package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

/**
 * Created by Andrey Germanov on 1/5/19.
 */
open class PlaceFragment: Fragment() {

    private var currentPlaceId = ""
    protected lateinit var viewModel:PlacesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_place,container,false)
        setViewModel()
        setListeners(view)
        return view
    }

    fun setViewModel() {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(PlacesViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    open fun setListeners(view:View) {
        viewModel.getCurrentPlaceId().observe(this, Observer<String> { id ->
            currentPlaceId = id
            val place = viewModel.getPlaces().value?.find {it.id == currentPlaceId }
            view.findViewById<EditText>(R.id.place_name).setText(place?.name ?: "")
            view.findViewById<EditText>(R.id.place_latitude).setText(place?.latitude?.toString() ?: "0.0")
            view.findViewById<EditText>(R.id.place_longitude).setText(place?.longitude?.toString() ?: "0.0")
        })
    }
}
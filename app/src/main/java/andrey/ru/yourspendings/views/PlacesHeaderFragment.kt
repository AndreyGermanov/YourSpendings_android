package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer

/**
 * Created by Andrey Germanov on 1/5/19.
 */
class PlacesHeaderFragment: PlaceFragment() {

    private lateinit var backButton: ImageButton
    private lateinit var headerTitle: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_places_header,container,false)
        setViewModel()
        setupView(view)
        setListeners(view)
        return view
    }

    fun setupView(view:View) {
        backButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener{ viewModel.setPlacesScreenMode(PlacesScreenMode.LIST) }
        headerTitle = view.findViewById(R.id.header_title)
    }

    override fun setListeners(view:View) {
        viewModel.getPlacesScreenMode().observe(this, Observer { mode ->
            switchHeaderMode(mode,viewModel.isLandscapeMode())
        })
        viewModel.getLandscape().observe(this, Observer { isLandscape ->
            switchHeaderMode(viewModel.getPlacesScreenMode().value!!,isLandscape)
        })
    }

    fun switchHeaderMode(mode:PlacesScreenMode,isLandscape:Boolean) {
        if (isLandscape) {
            backButton.visibility = View.INVISIBLE
            headerTitle.text = getString(R.string.places_list)
            return
        }
        when (mode) {
            PlacesScreenMode.LIST -> {
                backButton.visibility = View.INVISIBLE
                headerTitle.text = getString(R.string.places_list)
            }
            PlacesScreenMode.ITEM -> {
                headerTitle.text = viewModel.getCurrentPlace()?.name ?: ""
                backButton.visibility = View.VISIBLE
            }
        }
    }
}
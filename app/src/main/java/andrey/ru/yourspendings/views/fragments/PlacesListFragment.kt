package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Place
import andrey.ru.yourspendings.views.adapters.PlacesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrey Germanov on 1/5/19.
 */
class PlacesListFragment: PlaceFragment() {

    private lateinit var places:List<Place>
    private lateinit var placesAdapter:PlacesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_places,container,false)
        setViewModel()
        setupList(view)
        setListeners(view)
        return view
    }

    override fun setViewModel() {
        super.setViewModel()
        places = viewModel.getPlaces().value ?: ArrayList()
    }

    override fun setListeners(view:View) {
        viewModel.getPlaces().observe(this, Observer<List<Place>>{ placesList ->
            this.places = placesList
            placesAdapter.apply { setDataSet(placesList); notifyDataSetChanged()}
        })
        viewModel.getCurrentPlaceId().observe(this, Observer<String> { placeId ->
            this.currentPlaceId = placeId
            placesAdapter.notifyDataSetChanged()
        })
    }

    private fun setupList(view:View) {
        placesAdapter = PlacesAdapter(places,viewModel)
        val viewManager = LinearLayoutManager(this.context)
        view.findViewById<RecyclerView>(R.id.places_list_container).apply {
            layoutManager = viewManager
            adapter = placesAdapter
        }
    }


}
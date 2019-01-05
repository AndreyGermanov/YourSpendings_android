package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Place
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrey Germanov on 1/5/19.
 */
class PlacesListFragment: PlaceFragment() {

    private var places:List<Place> = ArrayList()
    private var currentPlaceId:String = ""
    private lateinit var placesAdapter:PlacesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_places,container,false)
        setViewModel()
        setupList(view)
        setListeners(view)
        return view
    }

    override fun setListeners(view:View) {
        viewModel.getPlaces().observe(this, Observer<List<Place>>{ placesList ->
            this.places = placesList
            placesAdapter.setDataSet(placesList)
            placesAdapter.notifyDataSetChanged()
        })
        viewModel.getCurrentPlaceId().observe(this, Observer<String> { placeId ->
            this.currentPlaceId = placeId
            placesAdapter.notifyDataSetChanged()
        })
    }

    private fun setupList(view:View) {
        placesAdapter = PlacesAdapter(places)
        val viewManager = LinearLayoutManager(this.context)
        view.findViewById<RecyclerView>(R.id.places_list_container).apply {
            layoutManager = viewManager
            adapter = placesAdapter
        }
    }

    inner class PlacesAdapter(private var dataset: List<Place>):RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

         inner class ViewHolder(val item:View): RecyclerView.ViewHolder(item)

         override fun getItemCount(): Int = dataset.size

         fun setDataSet(dataset: List<Place>) { this.dataset = dataset }

         override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
             ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.places_list_item,parent,false))


         override fun onBindViewHolder(holder: ViewHolder, position: Int) {
             val placeLabel = holder.item.findViewById<TextView>(R.id.place_title)
             placeLabel.text = dataset[position].name
             holder.item.setOnClickListener {
                 viewModel.setCurrentPlaceId(dataset[position].id)
                 viewModel.setPlacesScreenMode(PlacesScreenMode.ITEM)
             }
             if (currentPlaceId == dataset[position].id)
                 holder.item.setBackgroundColor(Color.GRAY)
             else
                 holder.item.setBackgroundColor(Color.WHITE)
         }
     }
}
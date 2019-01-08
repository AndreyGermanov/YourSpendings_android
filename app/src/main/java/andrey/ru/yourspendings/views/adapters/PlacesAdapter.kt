package andrey.ru.yourspendings.views.adapters

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Place
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import andrey.ru.yourspendings.views.viewmodels.PlacesViewModel
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrey Germanov on 1/7/19.
 */
class PlacesAdapter(private var dataset: List<Place>,
                    private var viewModel: PlacesViewModel
): RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    inner class ViewHolder(val item: View): RecyclerView.ViewHolder(item)

    override fun getItemCount(): Int = dataset.size

    fun setDataSet(dataset: List<Place>) { this.dataset = dataset }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.places_list_item,parent,false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val placeLabel = holder.item.findViewById<TextView>(R.id.place_title)
        placeLabel.text = dataset[position].name
        holder.item.setOnClickListener {
            viewModel.setCurrentItemId(dataset[position].id)
            viewModel.setScreenMode(ScreenMode.ITEM)
        }
        if (viewModel.getCurrentItemId().value == dataset[position].id)
            holder.item.setBackgroundColor(Color.GRAY)
        else
            holder.item.setBackgroundColor(Color.WHITE)
    }
}
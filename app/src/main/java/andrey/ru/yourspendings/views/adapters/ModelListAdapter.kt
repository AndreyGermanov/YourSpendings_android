package andrey.ru.yourspendings.views.adapters

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.viewmodels.EntityViewModel
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrey Germanov on 1/7/19.
 */
class ModelListAdapter<T: Model>(private var dataset: List<T>,
                                 private var viewModel: EntityViewModel<T>
): RecyclerView.Adapter<ModelListAdapter<T>.ViewHolder>() {

    inner class ViewHolder(val item: View): RecyclerView.ViewHolder(item)

    override fun getItemCount(): Int = dataset.size

    fun setDataSet(dataset: List<T>) { this.dataset = dataset }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.model_list_item,parent,false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val label = holder.item.findViewById<TextView>(R.id.place_title)
        label.text = dataset[position].getTitle()
        holder.item.setOnClickListener {
            viewModel.setCurrentItemId(dataset[position].id)
            if (!viewModel.isSelectMode())
                viewModel.setScreenMode(ScreenMode.ITEM)
        }
        if (viewModel.getCurrentItemId().value == dataset[position].id)
            holder.item.setBackgroundColor(Color.GRAY)
        else
            holder.item.setBackgroundColor(Color.WHITE)
    }
}
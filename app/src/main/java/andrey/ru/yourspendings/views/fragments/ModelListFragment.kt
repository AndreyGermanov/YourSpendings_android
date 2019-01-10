package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.adapters.ModelListAdapter
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrey Germanov on 1/9/19.
 */
open class ModelListFragment<T: Model>: ModelFragment<T>() {

    override var fragmentId:Int = R.layout.fragment_model_list
    override var className:String = ""

    private lateinit var items:List<T>
    private lateinit var listAdapter: ModelListAdapter<T>

    override fun bindUI(view: View) { setupList(view) }

    override fun setListeners(view: View) {
        viewModel.getItems().observe(this, Observer<List<T>>{ list ->
            this.items = list
            listAdapter.apply { setDataSet(list); notifyDataSetChanged()}
        })
        viewModel.getCurrentItemId().observe(this, Observer<String> { itemId ->
            this.currentItemId = itemId
            listAdapter.notifyDataSetChanged()
        })
    }

    private fun setupList(view: View) {
        listAdapter = ModelListAdapter(viewModel.getItems().value ?: ArrayList(),viewModel)
        val viewManager = LinearLayoutManager(this.context)
        view.findViewById<RecyclerView>(R.id.list_container).apply {
            layoutManager = viewManager
            adapter = listAdapter
        }
    }
}
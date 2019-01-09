package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.adapters.EntityListAdapter
import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("ValidFragment")
/**
 * Created by Andrey Germanov on 1/9/19.
 */
open class EntityListFragment<T: Model>(
    override var fragmentId:Int,
    override var className:String): EntityScreenFragment<T>(fragmentId,className) {

    private lateinit var items:List<T>
    private lateinit var listAdapter: EntityListAdapter<T>

    override fun setViewModel() {
        super.setViewModel()
        items = viewModel.getItems().value ?: ArrayList()
    }

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
        listAdapter = EntityListAdapter<T>(items,viewModel)
        val viewManager = LinearLayoutManager(this.context)
        view.findViewById<RecyclerView>(R.id.places_list_container).apply {
            layoutManager = viewManager
            adapter = listAdapter
        }
    }
}
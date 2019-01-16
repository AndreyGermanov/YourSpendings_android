package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.adapters.ModelListAdapter
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import android.widget.Button
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
    private lateinit var selectBtn: Button
    private lateinit var editBtn:Button

    override fun bindUI(view: View) {
        with(view) {
            selectBtn = findViewById(R.id.select_item)
            editBtn = findViewById(R.id.edit_item)
        }
        setupList(view)
        setupButtons()
    }

    private fun setupList(view: View) {
        listAdapter = ModelListAdapter(viewModel.items, viewModel)
        view.findViewById<RecyclerView>(R.id.list_container).apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = listAdapter
        }
        listAdapter.notifyDataSetChanged()
    }

    private fun setupButtons() {
        val visibility = if (viewModel.selectMode == true) View.VISIBLE;else View.GONE
        val clickable = viewModel.currentItemId.isNotEmpty()
        selectBtn.apply { setVisibility(visibility);isClickable = clickable }
        editBtn.apply { setVisibility(visibility);isClickable = clickable }
    }

    override fun setListeners(view: View) {
        viewModel.itemsObserver.observe(this, Observer<List<T>>{ list ->
            this.items = list
            listAdapter.apply { setDataSet(list); notifyDataSetChanged()}
        })
        viewModel.currentItemIdObserver.observe(this, Observer<String> { itemId ->
            this.currentItemId = itemId
            listAdapter.notifyDataSetChanged()
            setupButtons()
        })

        editBtn.setOnClickListener { viewModel.screenMode = ScreenMode.ITEM }

        selectBtn.setOnClickListener {
            with(activity!!) {
                setResult(RESULT_OK,Intent().apply {
                    putExtra("subscriberId",arguments?.getString("subscriberId") ?: "")
                    putExtra("selectedItemId",currentItemId)
                })
                finish()
            }
        }
    }
}
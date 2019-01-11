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
        selectBtn = view.findViewById(R.id.select_item)
        editBtn = view.findViewById(R.id.edit_item)
        setupList(view)
        setupButtons()
    }

    fun setupButtons() {
        val visibility = if (viewModel.isSelectMode()) View.VISIBLE;else View.GONE
        selectBtn.visibility = visibility
        editBtn.visibility = visibility
        val clickable = viewModel.getCurrentItemId().value?.isNotEmpty() ?: false
        selectBtn.isClickable = clickable
        editBtn.isClickable = clickable
    }

    override fun setListeners(view: View) {
        viewModel.getItems().observe(this, Observer<List<T>>{ list ->
            this.items = list
            listAdapter.apply { setDataSet(list); notifyDataSetChanged()}
        })
        viewModel.getCurrentItemId().observe(this, Observer<String> { itemId ->
            this.currentItemId = itemId
            listAdapter.notifyDataSetChanged()
            setupButtons()
        })

        editBtn.setOnClickListener { viewModel.setScreenMode(ScreenMode.ITEM) }

        selectBtn.setOnClickListener {
            val intent = Intent()
            intent.putExtra("subscriberId",arguments?.getString("subscriberId") ?: "")
            intent.putExtra("selectedItemId",currentItemId)
            activity!!.setResult(RESULT_OK,intent)
            activity!!.finish()
        }
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
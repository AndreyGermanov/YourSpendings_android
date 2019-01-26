package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.adapters.ModelListAdapter
import andrey.ru.yourspendings.views.store.ModelState
import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrey Germanov on 1/19/19.
 */
@SuppressLint("ViewConstructor")
class ModelListComponent(val context:MainActivity):Component(context) {

    private lateinit var modelList:RecyclerView
    lateinit var modelListAdapter:ModelListAdapter
    lateinit var selectButton:Button
    lateinit var editButton:Button
    private lateinit var isLoadingWidget: View

    var state: ModelState? = null

    override fun render() {
        removeAllViews()
        addView(inflate(context, R.layout.fragment_model_list,null))
        bindUI()
        setupList()
        updateUI()
    }

    override fun bindUI() {
        modelList = findViewById(R.id.list_container)
        selectButton = findViewById(R.id.select_item)
        editButton = findViewById(R.id.edit_item)
        isLoadingWidget = findViewById(R.id.is_loading)
    }

    private fun setupList() {
        val state = state ?: return
        modelList.apply {
            layoutManager = LinearLayoutManager(this@ModelListComponent.context)
            adapter = ModelListAdapter(state).apply { modelListAdapter = this; notifyDataSetChanged() }
        }
    }

    fun updateUI() {
        val state = state ?: return
        if (state.isLoading) {
            modelList.visibility = View.GONE
            isLoadingWidget.visibility = View.VISIBLE
        } else {
            modelList.visibility = View.VISIBLE
            isLoadingWidget.visibility = View.GONE
        }
        var visibility = View.GONE
        if (state.selectMode) visibility = View.VISIBLE
        editButton.visibility = visibility
        selectButton.visibility = visibility
    }
}
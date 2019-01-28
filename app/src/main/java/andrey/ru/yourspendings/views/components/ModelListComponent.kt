package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.adapters.ModelListAdapter
import andrey.ru.yourspendings.views.store.ModelState
import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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
        val state = state ?: return
        addView(LinearLayout(context).apply { layoutParams = fullScreen()
            orientation = LinearLayout.VERTICAL
            addView(TextView(context).apply {layoutParams = horizontal();visibility = View.GONE }
                .also { isLoadingWidget = it }
            )
            addView(RecyclerView(context).apply {layoutParams = horizontal().apply { weight = 10.0f}
                layoutManager = LinearLayoutManager(this@ModelListComponent.context)
                adapter = ModelListAdapter(state).apply { modelListAdapter = this; notifyDataSetChanged() }
            }.also { modelList = it })
            addView(button(context.getString(R.string.select)).also { selectButton = it })
            addView(button(context.getString(R.string.edit)).also { editButton = it })
        })
        updateUI()
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
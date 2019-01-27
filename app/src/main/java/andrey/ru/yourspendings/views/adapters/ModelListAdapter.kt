package andrey.ru.yourspendings.views.adapters

import andrey.ru.yourspendings.views.store.ModelScreenMode
import andrey.ru.yourspendings.views.store.ModelState
import andrey.ru.yourspendings.views.utils.toDp
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrey Germanov on 1/19/19.
 */
class ModelListAdapter(private var state: ModelState) : RecyclerView.Adapter<ModelListAdapter.ViewHolder>() {

    inner class ViewHolder(val item: View): RecyclerView.ViewHolder(item)

    override fun getItemCount(): Int  = state.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LinearLayout(parent.context).apply  { layoutParams = LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT)
                val margin =10.0f.toDp(parent.context)
                setPadding(margin,margin,margin,margin)
                addView(TextView(parent.context).apply { layoutParams = LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT)
                    tag = "title"
                })
            }
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val label:TextView = holder.item.findViewWithTag("title")
        label.text = state.items[position].getTitle()
        holder.item.setOnClickListener {
            state.currentItemId = state.items[position].id
            if (!state.selectMode)
                state.mode = ModelScreenMode.ITEM
        }
        if (state.currentItemId == state.items[position].id) {
            holder.item.setBackgroundColor(Color.GRAY)
        } else {
            holder.item.setBackgroundColor(Color.WHITE)
        }
    }
}
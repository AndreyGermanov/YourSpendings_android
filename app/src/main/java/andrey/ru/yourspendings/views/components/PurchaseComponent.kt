package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.adapters.ModelImagesAdapter
import andrey.ru.yourspendings.views.utils.toDp
import android.annotation.SuppressLint
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrey Germanov on 1/21/19.
 */
@SuppressLint("ViewConstructor")
class PurchaseComponent(override val context:MainActivity):ModelItemComponent(context) {

    lateinit var date:TextView
    lateinit var place:TextView
    lateinit var selectDateButton:ImageButton
    lateinit var selectPlaceButton:ImageButton
    lateinit var detectPlaceButton:ImageButton
    lateinit var takePictureButton: Button
    private lateinit var imagesContainer: RecyclerView
    lateinit var imagesAdapter: ModelImagesAdapter

    override fun renderLayout(): LinearLayout {
        val ctx = context
        return super.renderLayout().apply {
            addView(TextView(context).apply { text = context.resources.getString(R.string.images)},indexOfChild(buttonsContainer))
            addView(RecyclerView(context).apply {
                layoutParams = RecyclerView.LayoutParams(MATCH_PARENT,55.0f.toDp(context))
                adapter =  ModelImagesAdapter(ctx.store.state.purchasesState).apply { notifyDataSetChanged() }
                    .also { imagesAdapter = it }
                layoutManager =  LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            }.also { imagesContainer = it},indexOfChild(buttonsContainer))
            addView(button(context.resources.getString(R.string.take_a_picture))
                .also { takePictureButton = it},indexOfChild(buttonsContainer))
        }
    }

    override fun renderForm(): TableLayout {
        return super.renderForm().apply {
            addView(renderLabelRow("Date") { date = it }.apply {
                addView(selectButton().also { selectDateButton = it })
            })
            addView(renderLabelRow("Shop") { place = it}. apply {
                addView(selectButton().also { selectPlaceButton = it })
                addView(detectButton().also { detectPlaceButton = it })
            })
        }
    }

}
package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.adapters.ModelImagesAdapter
import android.annotation.SuppressLint
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
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

    override fun render() {
        addView(inflate(context, R.layout.fragment_purchase,null))
        bindUI()
        setupList()
    }

    override fun bindUI() {
        super.bindUI()
        date = findViewById(R.id.purchase_date)
        place = findViewById(R.id.purchase_shop)
        selectDateButton = findViewById(R.id.select_date_btn)
        selectPlaceButton = findViewById(R.id.select_place_btn)
        detectPlaceButton = findViewById(R.id.detect_place_btn)
        imagesContainer = findViewById(R.id.images_list_container)
        takePictureButton = findViewById(R.id.take_picture_button)
    }

    private fun setupList() {
        imagesAdapter = ModelImagesAdapter(context.store.state.purchasesState)
        imagesContainer.apply {
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        }
        imagesAdapter.notifyDataSetChanged()
    }
}
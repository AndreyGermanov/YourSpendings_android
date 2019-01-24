package andrey.ru.yourspendings.views.adapters

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.store.PurchasesState
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Andrey Germanov on 1/7/19.
 */
@Suppress("UNCHECKED_CAST")
class ModelImagesAdapter(private var state: PurchasesState): RecyclerView.Adapter<ModelImagesAdapter.ImagesViewHolder>() {

    inner class ImagesViewHolder(val item: View): RecyclerView.ViewHolder(item)

    override fun getItemCount(): Int {
        return state.images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder =
        ImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.model_item_image, parent, false)
        )

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val dataset = state.imagesList
        val imageView: ImageView = holder.item.findViewById(R.id.item_image)
        val path = "${state.imgCachePath}/${state.currentItemId}/${dataset[position]}.jpg"
        imageView.setImageBitmap(BitmapFactory.decodeFile(path))

        imageView.setOnClickListener {
            state.currentImageId = dataset[position]
            state.removeImageSelected = false
            state.imagesPagerOpened = true
        }

    }
}
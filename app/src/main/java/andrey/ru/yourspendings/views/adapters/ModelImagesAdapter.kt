package andrey.ru.yourspendings.views.adapters

import andrey.ru.yourspendings.views.store.PurchasesState
import andrey.ru.yourspendings.views.utils.toDp
import android.graphics.BitmapFactory
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
            LinearLayout(parent.context).apply {
                layoutParams = LinearLayout.LayoutParams(55.0f.toDp(parent.context),55.0f.toDp(parent.context))
                orientation = LinearLayout.VERTICAL
                addView(ImageView(parent.context).apply { tag = "image"
                    layoutParams = LinearLayout.LayoutParams(50.0f.toDp(parent.context),50.0f.toDp(parent.context))
                        .apply { gravity = Gravity.CENTER }
                })
            }
        )

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val dataset = state.imagesList
        val imageView: ImageView = holder.item.findViewWithTag("image")
        val path = "${state.imgCachePath}/${state.currentItemId}/${dataset[position]}.jpg"
        imageView.setImageBitmap(BitmapFactory.decodeFile(path))

        imageView.setOnClickListener {
            state.currentImageId = dataset[position]
            state.removeImageSelected = false
            state.imagesPagerOpened = true
        }

    }
}
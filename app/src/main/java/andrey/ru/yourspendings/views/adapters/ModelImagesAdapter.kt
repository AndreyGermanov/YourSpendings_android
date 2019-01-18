package andrey.ru.yourspendings.views.adapters

import andrey.ru.yourspendings.views.PurchaseImageActivity
import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.viewmodels.EntityViewModel
import andrey.ru.yourspendings.views.viewmodels.PurchasesViewModel
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap

/**
 * Created by Andrey Germanov on 1/7/19.
 */
@Suppress("UNCHECKED_CAST")
class ModelImagesAdapter<T: Model>(private var viewModel: EntityViewModel<T>,private var ctx: Context
): RecyclerView.Adapter<ModelImagesAdapter<T>.ImagesViewHolder>() {

    inner class ImagesViewHolder(val item: View): RecyclerView.ViewHolder(item)

    override fun getItemCount(): Int {

        return (viewModel.fields["images"] as? Map<String,String> ?: HashMap()).size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder =
        ImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.model_item_image, parent, false)
        )

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val dataset = (viewModel.fields["images"] as Map<String,String>).keys.map { it } as ArrayList<String>
        val viewModel = viewModel as PurchasesViewModel
        dataset.sort()
        val imageView: ImageView = holder.item.findViewById(R.id.item_image)
        val ctx: MainActivity = ctx as MainActivity

        val path = "${viewModel.imgCachePath}/${viewModel.currentItemId}/${dataset[position]}.jpg"
        imageView.setImageBitmap(BitmapFactory.decodeFile(path))
        imageView.setOnClickListener {
            ctx.run {
                startActivityForResult(Intent(this,PurchaseImageActivity::class.java).apply {
                    val itemId = this@ModelImagesAdapter.viewModel.currentItemId
                    putExtra("subscriberId",R.layout.fragment_purchase.toString()+"-"+itemId)
                    putExtra("currentItemId",position)
                    putExtra("images",dataset.map { "${viewModel.imgCachePath}/$itemId/$it.jpg"} as ArrayList<String>)
                },REQUEST_PURCHASE_IMAGE_TO_REMOVE)
            }
        }
    }
}
package andrey.ru.yourspendings.views.adapters

import andrey.ru.yourspendings.views.PurchaseImageActivity
import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.viewmodels.EntityViewModel
import android.content.Intent
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
class ModelImagesAdapter<T: Model>(private var viewModel: EntityViewModel<T>
): RecyclerView.Adapter<ModelImagesAdapter<T>.ImagesViewHolder>() {

    inner class ImagesViewHolder(val item: View): RecyclerView.ViewHolder(item)

    override fun getItemCount(): Int = ((viewModel.getFields()["images"] ?: HashMap<String,String>()) as HashMap<String,String>).size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder =
        ImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.model_item_image, parent, false)
        )

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val dataset = (viewModel.getFields()["images"] as HashMap<String,String>).keys.map { it } as ArrayList<String>
        dataset.sort()
        val imageView: ImageView = holder.item.findViewById(R.id.item_image)
        val ctx: MainActivity = viewModel.getContext() as MainActivity
        val path = ctx.filesDir.absolutePath+"/images/"+viewModel.getCurrentItemId().value+"/"+dataset[position]+".jpg"
        imageView.setImageBitmap(BitmapFactory.decodeFile(path))
        imageView.setOnClickListener {
            ctx.run {
                startActivityForResult(Intent(this,PurchaseImageActivity::class.java).apply {
                    val itemId = this@ModelImagesAdapter.viewModel.getCurrentItemId().value
                    putExtra("subscriberId",R.layout.fragment_purchase.toString()+"-"+itemId)
                    putExtra("currentItemId",position)
                    putExtra("images",dataset.map { filesDir.absolutePath+"/images/"+itemId+"/"+it+".jpg"} as ArrayList<String>)
                },REQUEST_PURCHASE_IMAGE_TO_REMOVE)
            }
        }
    }
}
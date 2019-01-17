package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.models.PurchasesCollection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Andrey Germanov on 1/9/19.
 */
class PurchasesViewModel: EntityViewModel<Purchase>(Collection = PurchasesCollection) {

    private var mImagePath:String = ""
    var imagePath:String
        get() = mImagePath
        set(value) { mImagePath = value;save() }

    val imgCloudPath
        get() = (Collection as PurchasesCollection).imgCloudPath
    val imgCachePath
        get() = (Collection as PurchasesCollection).imgCachePath


    fun syncImageCache(callback:()->Unit) {
        val item = this.getCurrentItem()
        if (item != null) {
            (Collection as PurchasesCollection).syncImageCache(item.id, item.images) {
                callback()
            }
        } else {
            callback()
        }
    }

    override fun getState(): HashMap<String, Any> {
        val state = super.getState()
        state["imagePath"] = mImagePath
        val fields = state["fields"] as HashMap<String,Any>
        val date = fields["date"] as? LocalDateTime ?: LocalDateTime.now()
        fields["date"] = date.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"))
        state["fields"] = fields
        return state
    }

    override fun setState(state: HashMap<String, Any>) {
        super.setState(state)
        mImagePath = state["imagePath"]?.toString() ?: ""
    }
}
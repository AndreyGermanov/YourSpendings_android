package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.models.PurchasesCollection

/**
 * Created by Andrey Germanov on 1/9/19.
 */
object PurchasesViewModel: EntityViewModel<Purchase>(Collection = PurchasesCollection) {

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
        if (item != null)
            (Collection as PurchasesCollection).syncImageCache(item.id,item.images) { callback() }
    }

    override fun getState(): HashMap<String, Any> {
        return super.getState().apply { put("imagePath",mImagePath)}
    }

    override fun setState(state: HashMap<String, Any>) {
        super.setState(state)
        mImagePath = state["imagePath"]?.toString() ?: ""
    }
}
package andrey.ru.yourspendings.views.viewmodels

import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by Andrey Germanov on 1/13/19.
 */
object PurchaseImageViewModel: PersistedViewModel() {
    private var mCurrentImageId = -1
    var currentImageId:Int
        get() = mCurrentImageId
        set(value) { mCurrentImageId=value;save()}
    private var mImages:ArrayList<String> = ArrayList()
    var images:ArrayList<String>
        get() = mImages
        set(value) { mImages = value;save()}
    private var mSubscriberId = ""
    var subscriberId
        get() = mSubscriberId
        set(value) { mSubscriberId = value;save()}

    override fun getState(): HashMap<String, Any> = hashMapOf(
        "subscriberId" to mSubscriberId,
        "currentImageId" to mCurrentImageId,
        "images" to mImages
    )

    override fun setState(state:HashMap<String,Any>) {
        mSubscriberId = state["subscriberId"]?.toString() ?: ""
        mCurrentImageId = state["mCurrentImageId"]?.toString()?.toInt() ?: -1
        mImages = state["mImages"] as? LinkedList<String> as? ArrayList<String> ?: ArrayList()
    }
}
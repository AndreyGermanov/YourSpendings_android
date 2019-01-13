package andrey.ru.yourspendings.views.viewmodels

import androidx.lifecycle.ViewModel

/**
 * Created by Andrey Germanov on 1/13/19.
 */
class PurchaseImageViewModel: ViewModel() {
    var currentImageId = -1
    var images:ArrayList<String> = ArrayList()
    var subscriberId = ""
}
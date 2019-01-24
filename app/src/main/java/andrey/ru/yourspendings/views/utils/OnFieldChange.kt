package andrey.ru.yourspendings.views.utils

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by Andrey Germanov on 1/22/19.
 */
class OnFieldChange(val callback:(text:String)->Unit): TextWatcher {
    override fun afterTextChanged(p0: Editable?) { callback(p0.toString()) }
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

}
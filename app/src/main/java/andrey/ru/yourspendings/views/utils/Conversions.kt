package andrey.ru.yourspendings.views.utils

import android.content.Context
import android.util.TypedValue

/**
 * Created by Andrey Germanov on 1/27/19.
 */
fun Float.toDp(context: Context):Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics).toInt()
}
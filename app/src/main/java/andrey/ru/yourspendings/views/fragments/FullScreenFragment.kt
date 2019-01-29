package andrey.ru.yourspendings.views.fragments

import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

/**
 * Created by Andrey Germanov on 1/29/19.
 */
abstract class FullScreenFragment: DialogFragment() {

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }
}
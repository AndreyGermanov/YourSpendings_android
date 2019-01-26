package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.containers.Container
import andrey.ru.yourspendings.views.containers.DateTimePickerContainer
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
 * Created by Andrey Germanov on 1/11/19.
 */
class DateTimePickerFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity as MainActivity
        val state = activity.store.state.dateTimePickerState
        val view = Container.getInstance(activity,DateTimePickerContainer::class.java).setView()
        return activity.let {
            AlertDialog.Builder(it).setView(view)
            .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                state.dateSelected = true
                state.confirmed = true
            }
            .setNegativeButton("Cancel") {_:DialogInterface,_:Int ->
                state.dateSelected = false
                state.confirmed = true
            }.create().apply {

                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.AppTheme)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        val activity = activity as MainActivity
        val state = activity.store.state.dateTimePickerState
        state.dateSelected = false
        state.confirmed = true
    }

}
package andrey.ru.yourspendings.views.fragments.ui

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.viewmodels.ui.DateTimePickerViewModel
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import java.lang.ClassCastException
import java.time.LocalDateTime

/**
 * Created by Andrey Germanov on 1/11/19.
 */
class DateTimePickerFragment: DialogFragment() {

    private lateinit var viewModel: DateTimePickerViewModel
    private lateinit var dialogView: AlertDialog
    private lateinit var delegate: DialogFragmentListener
    private lateinit var subscriberId: String
    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.fragment_date_time_picker,null)
        dialogView = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(view)
            .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                delegate.onPositiveButtonClicked(subscriberId,getDateTime().plusMonths(1))
            }
                .setNegativeButton("Cancel") {_:DialogInterface,_:Int ->}
            builder.create()
        }!!
        setViewModel()
        bindUI(view)
        setListeners()
        return dialogView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            delegate = context as DialogFragmentListener
        } catch (e: ClassCastException){
            throw ClassCastException("$context should implement DateTimeDialogListener interface")
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this).get(DateTimePickerViewModel::class.java)
        subscriberId = arguments?.getString("subscriberId").toString()
        if (viewModel.getDate() == null) viewModel.setDate(arguments?.getSerializable("initialDateTime") as LocalDateTime)
    }

    private fun bindUI(view: View) {
        datePicker = view.findViewById(R.id.date_picker)
        timePicker = view.findViewById(R.id.time_picker)

        val date = viewModel.getDate()!!
        datePicker.updateDate(date.year, date.monthValue, date.dayOfMonth)
        timePicker.hour = date.hour
        timePicker.minute = date.minute
    }

    private fun setListeners() {
        datePicker.setOnDateChangedListener {_,year,month,day ->
            val date = viewModel.getDate()!!
            viewModel.setDate(LocalDateTime.of(year,month,day,date.hour,date.minute))
        }
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            val date = viewModel.getDate()!!
            viewModel.setDate(LocalDateTime.of(date.year,date.monthValue,date.dayOfMonth,hour,minute))
        }
    }

    private fun getDateTime():LocalDateTime {
        return LocalDateTime.of(
            datePicker.year, datePicker.month, datePicker.dayOfMonth,
            timePicker.hour, timePicker.minute
        )
    }

}
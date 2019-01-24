package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.widget.DatePicker
import android.widget.TimePicker

/**
 * Created by Andrey Germanov on 1/23/19.
 */
class DateTimePickerComponent(val context:MainActivity): Component(context) {

    lateinit var datePicker: DatePicker
    lateinit var timePicker: TimePicker

    override fun render() {
        addView(inflate(context, R.layout.fragment_date_time_picker,null))
        bindUI()
    }

    override fun bindUI() {
        datePicker = findViewById(R.id.date_picker)
        timePicker = findViewById(R.id.time_picker)
    }
}
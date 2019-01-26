package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TimePicker

/**
 * Created by Andrey Germanov on 1/23/19.
 */
@SuppressLint("ViewConstructor")
class DateTimePickerComponent(val context:MainActivity): Component(context) {

    lateinit var datePicker: DatePicker
    lateinit var timePicker: TimePicker

    override fun render() {
        addView(LinearLayout(context).apply { layoutParams = fullScreen()
            orientation = LinearLayout.VERTICAL
            addView(DatePicker(context).apply { layoutParams = horizontal(); datePicker = this } )
            addView(TimePicker(context).apply { layoutParams = horizontal(); timePicker = this } )
        })
    }
}
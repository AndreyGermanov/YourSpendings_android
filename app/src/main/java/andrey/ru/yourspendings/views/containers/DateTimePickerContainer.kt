package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.DateTimePickerComponent
import andrey.ru.yourspendings.views.store.DateTimePickerState

/**
 * Created by Andrey Germanov on 1/23/19.
 */
class DateTimePickerContainer: Container() {

    lateinit var view:DateTimePickerComponent
    lateinit var state:DateTimePickerState

    override fun initialize(context: MainActivity) {
        super.initialize(context)
        view = DateTimePickerComponent(context)
        component = view
        state = context.store.state.dateTimePickerState
        initComponent()
        setListeners()
    }

    private fun initComponent() {
        view.render()
        updateUI()
    }

    private fun updateUI() {
        view.datePicker.updateDate(state.year,state.month,state.day)
        view.timePicker.hour = state.hour
        view.timePicker.minute = state.minute
    }

    fun setListeners() {
        view.datePicker.setOnDateChangedListener { _, year, month, day ->
            state.year = year
            state.month = month
            state.day = day
        }
        view.timePicker.setOnTimeChangedListener { _, hour, minute ->
            state.hour = hour
            state.minute = minute
        }
    }

}
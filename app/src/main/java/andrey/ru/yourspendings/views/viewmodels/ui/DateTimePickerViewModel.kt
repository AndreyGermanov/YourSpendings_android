package andrey.ru.yourspendings.views.viewmodels.ui

import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

/**
 * Created by Andrey Germanov on 1/11/19.
 */
class DateTimePickerViewModel: ViewModel() {

    private var date: LocalDateTime? = null

    fun getDate() = date
    fun setDate(dt: LocalDateTime) { date = dt}
}
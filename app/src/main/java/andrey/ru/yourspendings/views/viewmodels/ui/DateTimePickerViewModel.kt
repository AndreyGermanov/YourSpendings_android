package andrey.ru.yourspendings.views.viewmodels.ui

import andrey.ru.yourspendings.views.viewmodels.PersistedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Andrey Germanov on 1/11/19.
 */
@Suppress("UNCHECKED_CAST")
object DateTimePickerViewModel: PersistedViewModel() {

    private var mDate: LocalDateTime? = null
    val formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")

    var date:LocalDateTime?
        get() = mDate
        set(value) { mDate=value;save() }

    override fun getState(): HashMap<String, Any> = hashMapOf(
        "date" to (mDate?.format(formatter) ?: LocalDateTime.now().format(formatter))
    )

    override fun setState(state: HashMap<String, Any>) {
        mDate = LocalDateTime.parse(state["date"]?.toString() ?: LocalDateTime.now().format(formatter),formatter)
    }

}
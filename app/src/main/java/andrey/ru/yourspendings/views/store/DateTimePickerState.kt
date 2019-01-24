package andrey.ru.yourspendings.views.store

import andrey.ru.yourspendings.extensions.dateFromAny
import java.time.LocalDateTime

/**
 * Created by Andrey Germanov on 1/23/19.
 */
@Suppress("UNCHECKED_CAST")
class DateTimePickerState(override val state:AppState): BaseState(state,"DateTimePicker") {

    override fun initialize() {
        state.fieldSettings[index] = hashMapOf(
            "confirmed" to hashMapOf("transient" to true),
            "dateSelected" to hashMapOf("dateSelected" to true)
        ) as HashMap<String,Any>
    }

    var year:Int
        get() = getIntValue("year",1971)
        set(value) { setValue("year",value)}

    var month:Int
        get() = getIntValue("month",1)
        set(value) { setValue("month", value) }

    var day:Int
        get() = getIntValue("day",1)
        set(value) { setValue("day", value) }

    var hour:Int
        get() = getIntValue("hour")
        set(value) { setValue("hour", value) }

    var minute:Int
        get() = getIntValue("minute")
        set(value) { setValue("minute", value)}

    var second:Int
        get() = getIntValue("second")
        set(value) { setValue("second",value) }

    var subscriberId:String
        get() = getStringValue("subscriberId")
        set(value) { setValue("subscriberId",value) }

    var confirmed:Boolean
        get() = getBooleanValue("confirmed")
        set(value) { setValue("confirmed",value) }

    var dateSelected:Boolean
        get() = getBooleanValue("dateSelected")
        set(value) { setValue("dateSelected", value) }

}
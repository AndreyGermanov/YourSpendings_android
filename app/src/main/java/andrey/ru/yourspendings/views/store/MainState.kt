package andrey.ru.yourspendings.views.store

import android.content.res.Configuration


/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
class MainState(override val state:AppState):BaseState(state,"MainState") {

    override fun initialize() {
        state.fieldSettings[index] = hashMapOf(
            "orientation" to hashMapOf("transient" to true),
            "openDrawer" to hashMapOf("transient" to true),
            "lifecycleState" to hashMapOf("transient" to true)
        ) as HashMap<String,Any>
    }
    var screen:Screen
        get() = Screen.valueOf(getValue("screen")?.toString() ?: "LOGIN")
        set(value) { setValue("screen",value.toString())}

    var orientation: Int
        get() = getValue("orientation")?.toString()?.toDoubleOrNull()?.toInt() ?: Configuration.ORIENTATION_PORTRAIT
        set(value) { setValue("orientation",value) }

    var openDrawer: Boolean
        get() = getValue("openDrawer")?.toString()?.toBoolean() ?: false
        set(value) { setValue("openDrawer",value)}

    var lifecycleState: LifecycleState
        get() = LifecycleState.valueOf(getValue("lifecycleState")?.toString() ?: "ON_IDLE" )
        set(value) { setValue("lifecycleState",value.toString())}
}



enum class Screen {LOGIN,DASHBOARD,PLACES,PURCHASES}

enum class LifecycleState { ON_CREATE,ON_PAUSE, ON_RESUME, ON_IDLE, ON_START, ON_STOP, ON_LOW_MEMORY, ON_DESTROY }
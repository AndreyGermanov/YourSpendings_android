package andrey.ru.yourspendings.views.store

import com.google.gson.Gson


/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
class MainState(override val state:AppState):BaseState(state,"MainState") {

    var screen:Screen
        get() = Screen.valueOf(getValue("screen")?.toString() ?: "LOGIN")
        set(value:Screen) { setValue("screen",value.toString())}

}



enum class Screen {LOGIN,DASHBOARD,PLACES,PURCHASES}
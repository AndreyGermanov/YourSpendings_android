package andrey.ru.yourspendings.views.store

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
class LoginState(override val state:AppState):BaseState(state,"LoginState") {

    override fun initialize() {
        state.fieldSettings[index] = hashMapOf(
            "loginPassword" to hashMapOf("transient" to true),
            "loginError" to hashMapOf("transient" to true),
            "registerPassword" to hashMapOf("transient" to true),
            "registerConfirmPassword" to hashMapOf("transient" to true),
            "registerError" to hashMapOf("stransient" to true)
        ) as HashMap<String,Any>
    }

    var mode:LoginMode
        get() = LoginMode.valueOf(getValue("mode")?.toString() ?: "LOGIN")
        set(value) { setValue("mode",value.toString())}

    var loginName:String
        get() = getValue("loginName")?.toString() ?: ""
        set(value) { setValue("loginName",value); }

    var loginPassword:String
        get() = getValue("loginPassword")?.toString() ?: ""
        set(value) { setValue("loginPassword",value)}

    var loginError:String
        get() = getValue("loginError")?.toString() ?: ""
        set(value) { setValue("loginError",value)}

    var registerName:String
        get() = getValue("registerName")?.toString() ?: ""
        set(value) { setValue("registerName",value)}

    var registerPassword: String
        get() = getValue( "registerPassword")?.toString() ?: ""
        set(value) { setValue("registerPassword",value)}

    var registerConfirmPassword: String
        get() = getValue("registerConfirmPassword")?.toString() ?: ""
        set(value) { setValue("registerConfirmPassword",value)}

    var registerError:String
        get() = getValue("registerError")?.toString() ?: ""
        set(value) { setValue("registerError",value)}

}

enum class LoginMode {LOGIN,REGISTER}
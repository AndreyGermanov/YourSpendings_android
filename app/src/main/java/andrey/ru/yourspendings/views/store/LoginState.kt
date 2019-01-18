package andrey.ru.yourspendings.views.store

import andrey.ru.yourspendings.views.viewmodels.LoginViewModel.fields

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
class LoginState(override val state:AppState):BaseState(state,"LoginState") {

    var mode:LoginMode
        get() = LoginMode.valueOf(getValue("mode")?.toString() ?: "LOGIN")
        set(value:LoginMode) { setValue("mode",value.toString())}

    var loginName:String
        get() = getValue("loginName")?.toString() ?: ""
        set(value:String) { setValue("loginName",value); }

    var loginPassword:String
        get() = getValue("loginPassword")?.toString() ?: ""
        set(value:String) { setValue("loginPassword",value)}

    var loginError:String
        get() = getValue("loginError")?.toString() ?: ""
        set(value:String) { setValue("loginError",value)}

    var registerName:String
        get() = getValue("registerName")?.toString() ?: ""
        set(value:String) { setValue("registerName",value)}

    var registerPassword: String
        get() = getValue( "registerPassword")?.toString() ?: ""
        set(value:String) { setValue("registerPassword",value)}

    var registerConfirmPassword: String
        get() = getValue("registerConfirmPassword")?.toString() ?: ""
        set(value:String) { setValue("registerConfirmPassword",value)}

    var registerError:String
        get() = getValue("registerError")?.toString() ?: ""
        set(value:String) { setValue("registerError",value)}

}

enum class LoginMode {LOGIN,REGISTER}
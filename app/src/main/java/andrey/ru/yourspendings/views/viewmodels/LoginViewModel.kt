package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.services.AuthManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Andrey Germanov on 1/7/19.
 */
class LoginViewModel: ViewModel() {

    private lateinit var fields:HashMap<String,String>
    private val mode: MutableLiveData<LoginMode> = MutableLiveData()

    init {
        clearFields();
        mode.postValue(LoginMode.LOGIN)
    }

    fun getFields() = fields
    fun setFields(fields:HashMap<String,String>) { this.fields = fields}
    fun clearFields() { fields = hashMapOf("login" to "","password" to "","confirmPassword" to "") }
    fun getLoginMode() = mode
    fun setLoginMode(newMode:LoginMode) = mode.postValue(newMode)

    fun login(callback:(error:String?)->Unit) {
        val result:String? = validateLogin()
        if (result != null) {callback(result);return}
        AuthManager.login(fields["login"]!!,fields["password"]!!) { error -> callback(error) }
    }

    fun register(callback:(error:String?)->Unit) {
        val result:String? = validateRegister()
        if (result != null) {callback(result);return}
        AuthManager.register(fields["login"]!!,fields["password"]!!) { error -> callback(error) }
    }

    fun validateLogin():String? {
        val login = fields["login"]?.trim() ?: ""
        val password = fields["password"]?.trim() ?: ""
        if (login.isEmpty()) return "Login should not be empty"
        if (password.isEmpty()) return "Password should not be empty"
        return null
    }

    fun validateRegister():String? {
        val result:String? = validateLogin()
        if (result != null) return result
        val password = fields["password"]?.trim() ?: ""
        val confirmPassword = fields["confirmPassword"]?.trim() ?: ""
        if (password != confirmPassword) return "Passwords should match"
        return null
    }

}

enum class LoginMode { LOGIN, REGISTER }
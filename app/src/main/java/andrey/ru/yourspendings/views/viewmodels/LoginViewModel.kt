package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.services.AuthManager
import androidx.lifecycle.MutableLiveData
import com.google.gson.internal.LinkedTreeMap

/**
 * Created by Andrey Germanov on 1/7/19.
 */
@Suppress("UNCHECKED_CAST")
object LoginViewModel: PersistedViewModel() {

    private var mFields:HashMap<String,String> = hashMapOf("login" to "","password" to "","confirmPassword" to "")

    var fields:HashMap<String,String>
        get() = mFields
        set(value) {
            mFields = value
            save()
        }

    private var mMode:LoginMode = LoginMode.LOGIN
    val modeObserver: MutableLiveData<LoginMode> = MutableLiveData()

    var mode:LoginMode
        get() = mMode
        set(value) {
            mMode = value
            modeObserver.postValue(value)
            save()
        }

    fun clearFields():HashMap<String,String> {
        fields = hashMapOf("login" to "","password" to "","confirmPassword" to "")
        return fields
    }

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

    private fun validateLogin():String? {
        val login = fields["login"]?.trim() ?: ""
        val password = fields["password"]?.trim() ?: ""
        if (login.isEmpty()) return "Login should not be empty"
        if (password.isEmpty()) return "Password should not be empty"
        return null
    }

    private fun validateRegister():String? {
        val result:String? = validateLogin()
        if (result != null) return result
        val password = fields["password"]?.trim() ?: ""
        val confirmPassword = fields["confirmPassword"]?.trim() ?: ""
        if (password != confirmPassword) return "Passwords should match"
        return null
    }

    override fun getState():HashMap<String,Any> = hashMapOf(
        "fields" to fields,
        "mode" to mode
    )

    override fun setState(state:HashMap<String,Any>) {
        mFields = ((state["fields"] as? LinkedTreeMap<String,String> ?: LinkedTreeMap()).toMap() as HashMap<String,String>)
        mMode = LoginMode.valueOf(state["mode"]?.toString() ?: "LOGIN")
    }
}

enum class LoginMode { LOGIN, REGISTER }
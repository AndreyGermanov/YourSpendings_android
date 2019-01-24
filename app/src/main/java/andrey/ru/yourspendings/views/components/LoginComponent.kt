package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.store.LoginMode
import android.annotation.SuppressLint
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@SuppressLint("ViewConstructor")
class LoginComponent(context:MainActivity): Component(context) {

    lateinit var loginButton: Button
    lateinit var registerButton: Button
    lateinit var loginName: EditText
    lateinit var loginPassword: EditText
    lateinit var registerName: EditText
    lateinit var registerPassword: EditText
    lateinit var registerConfirmPassword: EditText
    lateinit var loginLink: TextView
    lateinit var registerLink: TextView

    var mode: LoginMode = LoginMode.LOGIN

    override fun render() {
        setScreen()
    }

    fun setScreen() {
        removeAllViews()
        when(mode) {
            LoginMode.LOGIN -> addView(inflate(context, R.layout.fragment_login,null))
            LoginMode.REGISTER -> addView(inflate(context, R.layout.fragment_register,null))
        }
        bindUI()
    }

    override fun bindUI() {
        when (mode) {
            LoginMode.LOGIN -> {
                loginName = findViewById(R.id.login_name)
                loginPassword = findViewById(R.id.login_password)
                loginButton = findViewById(R.id.login_button)
                registerLink = findViewById(R.id.register_link)
            }
            LoginMode.REGISTER -> {
                registerName = findViewById(R.id.register_name)
                registerPassword = findViewById(R.id.register_password)
                registerConfirmPassword = findViewById(R.id.register_confirm_password)
                registerButton = findViewById(R.id.register_button)
                loginLink = findViewById(R.id.login_link)
            }
        }
    }

}
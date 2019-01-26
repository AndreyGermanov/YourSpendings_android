package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.LoginComponent
import andrey.ru.yourspendings.views.store.AppState
import andrey.ru.yourspendings.views.store.LoginMode
import andrey.ru.yourspendings.views.store.Screen
import andrey.ru.yourspendings.views.utils.OnFieldChange
import android.widget.Toast

/**
 * Created by Andrey Germanov on 1/18/19.
 */
class LoginContainer: Container() {

    lateinit var view: LoginComponent

    override fun initialize(context:MainActivity) {
        super.initialize(context)
        view = LoginComponent(context)
        component = view
        initComponent()
        setListeners()
    }

    private fun initComponent() {
        val state = context.store.state.loginState
        with (view) {
            mode = state.mode
            render()
            when (mode) {
                LoginMode.LOGIN -> {
                    loginName.setText(state.loginName)
                    loginPassword.setText(state.loginPassword)
                }
                LoginMode.REGISTER -> {
                    registerName.setText(state.registerName)
                    registerPassword.setText(state.registerPassword)
                    registerConfirmPassword.setText(state.registerConfirmPassword)
                }
            }
        }
    }

    fun setListeners() {
        when (context.store.state.loginState.mode) {
            LoginMode.LOGIN -> setLoginListeners()
            LoginMode.REGISTER -> setRegisterListeners()
        }
    }

    private fun setLoginListeners() {
        view.loginName.addTextChangedListener(
            OnFieldChange { context.store.state.loginState.loginName = it }
        )
        view.loginPassword.addTextChangedListener(
            OnFieldChange { context.store.state.loginState.loginPassword = it }
        )
        view.loginButton.setOnClickListener { onLoginButtonClick() }
        view.registerLink.setOnClickListener { onRegisterLinkClick() }
    }

    private fun setRegisterListeners() {
        view.registerName.addTextChangedListener(
            OnFieldChange { context.store.state.loginState.registerName = it }
        )
        view.registerPassword.addTextChangedListener(
            OnFieldChange { context.store.state.loginState.registerPassword = it }
        )
        view.registerConfirmPassword.addTextChangedListener(
            OnFieldChange { context.store.state.loginState.registerConfirmPassword = it }
        )
        view.registerButton.setOnClickListener { onRegisterButtonClick() }
        view.loginLink.setOnClickListener { onLoginLinkClick() }
    }

    private fun onLoginButtonClick() {
        with (context.store.state.loginState) {
            val error = validateLogin(loginName,loginPassword)
            if (error != null) { loginError = error; return }
            AuthManager.login(loginName,loginPassword) {if (it != null) loginError = it }
        }
    }

    private fun validateLogin(login:String,password:String):String? {
        if (login.isEmpty()) return context.getString(R.string.error_empty_login)
        if (password.isEmpty()) return context.getString(R.string.error_empty_password)
        return null
    }

    private fun onRegisterButtonClick() {
        with(context.store.state.loginState) {
            val error = validateRegister(registerName,registerPassword,registerConfirmPassword)
            if (error != null) { registerError = error; return }
            AuthManager.register(registerName,registerPassword) { if (it != null) registerError = it }
        }
    }

    private fun validateRegister(login:String,password:String,confirmPassword:String):String? {
        val result = validateLogin(login,password)
        if (result != null) return result
        if (password != confirmPassword) return context.getString(R.string.error_passwords_should_match)
        return null
    }

    private fun onLoginLinkClick() {
        with (context.store.state.loginState) {
            mode = LoginMode.LOGIN
            loginName = ""
            loginPassword = ""
        }
    }

    private fun onRegisterLinkClick() {
        with (context.store.state.loginState) {
            mode = LoginMode.REGISTER
            registerName = ""
            registerPassword = ""
            registerConfirmPassword = ""
        }
    }

    override fun onStateChanged(state: AppState, prevState: AppState) {
        if (state.mainState.screen == Screen.LOGIN && prevState.mainState.screen != Screen.LOGIN) { onScreenOpen() }
        if (state.loginState.mode != prevState.loginState.mode) { onStateChangeMode(state.loginState.mode) }
        if (state.loginState.loginError != prevState.loginState.loginError) { onStateChangeLoginError() }
        if (state.loginState.registerError != prevState.loginState.registerError) { onStateChangeRegisterError() }
        super.onStateChanged(state, prevState)
    }

    private fun onScreenOpen() {
        with(context.store.state.loginState) {
            mode = andrey.ru.yourspendings.views.store.LoginMode.LOGIN
            loginName = ""
            loginPassword = ""
        }
        with (component as LoginComponent) {
            loginName.setText("")
            loginPassword.setText("")
        }
    }

    private fun onStateChangeMode(loginMode:LoginMode) {
        with(view) {
            mode = loginMode
            render()
            setListeners()
            when (loginMode) {
                LoginMode.LOGIN -> {
                    loginName.setText("")
                    loginPassword.setText("")
                }
                LoginMode.REGISTER -> {
                    registerName.setText("")
                    registerPassword.setText("")
                    registerConfirmPassword.setText("")
                }
            }
        }
    }

    private fun onStateChangeLoginError() {
        val state = context.store.state.loginState
        if (state.loginError.isNotEmpty()) {
            Toast.makeText(context, state.loginError, Toast.LENGTH_LONG).show()
            state.loginError = ""
        }
    }

    private fun onStateChangeRegisterError() {
        val state = context.store.state.loginState
        if (state.registerError.isNotEmpty()) {
            Toast.makeText(context, state.registerError, Toast.LENGTH_LONG).show()
            state.registerError = ""
        }
    }
}
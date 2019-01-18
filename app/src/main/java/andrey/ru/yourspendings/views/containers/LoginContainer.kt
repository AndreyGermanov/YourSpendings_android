package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.Component
import andrey.ru.yourspendings.views.components.LoginComponent
import andrey.ru.yourspendings.views.store.AppState
import andrey.ru.yourspendings.views.store.LoginMode
import andrey.ru.yourspendings.views.store.Screen
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

/**
 * Created by Andrey Germanov on 1/18/19.
 */
class LoginContainer(context:MainActivity): Container(context) {
    override var component: Component = LoginComponent(context)

    init {
        initComponent()
        setListeners()
    }

    fun initComponent() {
        val state = context.store.state.loginState
        with (component as LoginComponent) {
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
        val state = context.store.state
        when (state.loginState.mode) {
            LoginMode.LOGIN -> setLoginListeners()
            LoginMode.REGISTER -> setRegisterListeners()

        }
    }

    fun setLoginListeners() {
        val component = component as LoginComponent
        component.loginName.addTextChangedListener( object:TextWatcher {
            override fun afterTextChanged(p0: Editable?) { onLoginNameChange(component.loginName.text.toString())}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        component.loginPassword.addTextChangedListener( object:TextWatcher {
            override fun afterTextChanged(p0: Editable?) { onLoginPasswordChange(component.loginPassword.text.toString())}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        component.loginButton.setOnClickListener {onLoginButtonClick()}
        component.registerLink.setOnClickListener {onRegisterLinkClick()}
    }

    fun setRegisterListeners() {
        val component = component as LoginComponent
        component.registerName.addTextChangedListener( object:TextWatcher {
            override fun afterTextChanged(p0: Editable?) { onRegisterNameChange(component.registerName.text.toString())}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        component.registerPassword.addTextChangedListener( object:TextWatcher {
            override fun afterTextChanged(p0: Editable?) { onRegisterPasswordChange(component.registerPassword.text.toString())}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        component.registerConfirmPassword.addTextChangedListener( object:TextWatcher {
            override fun afterTextChanged(p0: Editable?) { onRegisterConfirmPasswordChange(component.registerConfirmPassword.text.toString())}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        component.registerButton.setOnClickListener {onRegisterButtonClick()}
        component.loginLink.setOnClickListener {onLoginLinkClick()}
    }

    fun onLoginNameChange(text:String) {
        context.store.state.loginState.loginName = text
    }

    fun onLoginPasswordChange(text:String) {
        context.store.state.loginState.loginPassword = text
    }

    fun onRegisterNameChange(text:String) {
        context.store.state.loginState.registerName = text
    }

    fun onRegisterPasswordChange(text:String) {
        context.store.state.loginState.registerPassword = text
    }

    fun onRegisterConfirmPasswordChange(text:String) {
        context.store.state.loginState.registerConfirmPassword = text
    }

    fun onLoginButtonClick() {
        with (context.store.state.loginState) {
            val error = validateLogin(loginName,loginPassword)
            if (error != null) { loginError = error; return }
            AuthManager.login(loginName,loginPassword) {if (it != null) loginError = it }
        }
    }

    fun validateLogin(login:String,password:String):String? {
        if (login.isEmpty()) return "Login should not be empty"
        if (password.isEmpty()) return "Password should not be empty"
        return null
    }

    fun onRegisterButtonClick() {}

    fun onLoginLinkClick() {
        with (context.store.state.loginState) {
            mode = LoginMode.LOGIN
            loginName = ""
            loginPassword = ""
        }
    }

    fun onRegisterLinkClick() {
        with (context.store.state.loginState) {
            mode = LoginMode.REGISTER
            registerName = ""
            registerPassword = ""
            registerConfirmPassword = ""
        }
    }

    override fun onStateChanged(state: AppState, prevState: AppState) {
        if (state.mainState.screen == Screen.LOGIN && prevState.mainState.screen != Screen.LOGIN) {
            with(state.loginState) {
                mode = LoginMode.LOGIN
                loginName = ""
                loginPassword = ""
            }
            with (component as LoginComponent) {
                loginName.setText("")
                loginPassword.setText("")
            }
        }
        if (state.loginState.mode != prevState.loginState.mode) {
            onStateChangeMode(state.loginState.mode)
        }
        if (state.loginState.loginError != prevState.loginState.loginError) {
            onStateChangeLoginError()
        }
        if (state.loginState.registerError != prevState.loginState.registerError) {
            onStateChangeRegisterError()
        }
        super.onStateChanged(state, prevState)
    }

    fun onStateChangeMode(loginMode:LoginMode) {
        with(component as LoginComponent) {
            mode = loginMode
            setScreen()
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

    fun onStateChangeLoginError() {
        val state = context.store.state.loginState
        if (state.loginError.isNotEmpty()) {
            Toast.makeText(context, state.loginError, Toast.LENGTH_LONG).show()
            state.loginError = ""
        }
    }

    fun onStateChangeRegisterError() {
        val state = context.store.state.loginState
        if (state.registerError.isNotEmpty()) {
            Toast.makeText(context, state.registerError, Toast.LENGTH_LONG).show()
            state.registerError = ""
        }
    }

}
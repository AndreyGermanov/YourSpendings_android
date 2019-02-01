package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.store.LoginMode
import android.annotation.SuppressLint
import android.view.View
import android.widget.*

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
        removeAllViews()
        addView(LinearLayout(context).apply { layoutParams = fullScreen()
            orientation = LinearLayout.VERTICAL
            setPadding(10,10,10,10)
            when(mode) {
                LoginMode.LOGIN -> renderLoginScreen(this)
                LoginMode.REGISTER -> renderRegisterScreen(this)
            }
        })
    }

    private fun renderLoginScreen(view:LinearLayout) {
        view.addView(TableLayout(context).apply { layoutParams = tableLayoutParams
            setColumnStretchable(1,true)
            addView(renderTextRow(context.getString(R.string.login)) { loginName = it })
            addView(renderPasswordRow(context.getString(R.string.password)) { loginPassword = it })
        })
        view.addView(button(context.getString(R.string.do_login)).also { loginButton = it })
        view.addView(renderLink(context.getString(R.string.sign_up)).also { registerLink = it })
    }

    private fun renderRegisterScreen(view:LinearLayout) {
        view.addView(TableLayout(context).apply { layoutParams = tableLayoutParams
            setColumnStretchable(1,true)
            addView(renderTextRow(context.getString(R.string.login)) { registerName = it })
            addView(renderPasswordRow(context.getString(R.string.password)) { registerPassword = it })
            addView(renderPasswordRow(context.getString(R.string.confirm)) { registerConfirmPassword = it })
        })
        view.addView(button(context.getString(R.string.register)).also { registerButton = it })
        view.addView(renderLink(context.getString(R.string.do_login)).also { loginLink = it })
    }

    private fun renderLink(title:String) = textView(title).apply {
        setTextColor(context.getColor(R.color.colorPrimary))
        isClickable = true
        isFocusable = true
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setPadding(0,10,0,0)
    }

}
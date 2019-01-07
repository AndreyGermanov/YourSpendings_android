package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.viewmodels.LoginMode
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
@SuppressLint("ValidFragment")

/**
 * Created by Andrey Germanov on 1/7/19.
 */
open class LoginFragment  (open var fragmentId:Int = R.layout.fragment_login): LoginContainerFragment(),View.OnKeyListener {

    protected lateinit var login: EditText
    protected lateinit var password: EditText
    protected lateinit var submitButton: Button
    protected lateinit var link: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(fragmentId,container,false)
        setupView(view)
        return view
    }

    private fun setupView(view:View) {
        setViewModel()
        bindUI(view)
        setListeners(view)
        loadFields()
    }

    open fun bindUI(view:View) {
        login = view.findViewById(R.id.login_text)
        password = view.findViewById(R.id.password_text)
        submitButton = view.findViewById(R.id.login_button)
        link = view.findViewById(R.id.login_link)
    }

    override fun setListeners(view:View) {
        submitButton.setOnClickListener {viewModel.login {error ->
                if (error != null) {
                    Toast.makeText(this.context,error,Toast.LENGTH_LONG).show()
                } else {
                    (activity as? MainActivity)?.setupScreen()
                }
            }
        }
        login.setOnKeyListener(this)
        password.setOnKeyListener(this)

        link.setOnClickListener { gotoLoginMode(LoginMode.REGISTER) }
    }

    protected fun gotoLoginMode(loginMode:LoginMode) {
        viewModel.clearFields()
        viewModel.setLoginMode(loginMode)
    }

    open fun getFields():HashMap<String,String> {
        return hashMapOf("login" to login.text.toString(),"password" to password.text.toString())
    }

    open fun loadFields() {
        val fields = viewModel.getFields()
        login.setText(fields["login"] ?: "")
        password.setText(fields["password"] ?: "")
    }

    private fun saveFields() = viewModel.setFields(getFields())

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        saveFields()
        return false
    }

}
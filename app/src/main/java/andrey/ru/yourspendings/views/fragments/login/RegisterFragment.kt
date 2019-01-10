package andrey.ru.yourspendings.views.fragments.login

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.viewmodels.LoginMode
import android.annotation.SuppressLint
import android.view.View
import android.widget.EditText
import android.widget.Toast

@SuppressLint("ValidFragment")

/**
 * Created by Andrey Germanov on 1/7/19.
 */
class RegisterFragment  (override var fragmentId:Int = R.layout.fragment_register): LoginFragment() {

    private lateinit var confirmPassword: EditText

    override fun bindUI(view: View) {
        super.bindUI(view)
        confirmPassword = view.findViewById(R.id.confirm_password_text)
    }

    override fun setListeners(view: View) {
        super.setListeners(view)

        submitButton.setOnClickListener {viewModel.register { error ->
            if (error != null) {
                Toast.makeText(this.context,error, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this.context,getString(R.string.register_success), Toast.LENGTH_LONG).show()
            }
        }}

        link.setOnClickListener { gotoLoginMode(LoginMode.LOGIN) }

        confirmPassword.setOnKeyListener(this)
    }

    override fun getFields():HashMap<String,String> {
        val result = super.getFields()
        result["confirmPassword"] = confirmPassword.text.toString()
        return result
    }

    override fun loadFields() {
        super.loadFields()
        val fields = viewModel.getFields()
        confirmPassword.setText(fields["confirmPassword"] ?: "")
    }
}
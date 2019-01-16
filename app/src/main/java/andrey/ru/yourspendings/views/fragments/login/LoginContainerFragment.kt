package andrey.ru.yourspendings.views.fragments.login

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.viewmodels.LoginMode
import andrey.ru.yourspendings.views.viewmodels.LoginViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

/**
 * Created by Andrey Germanov on 1/7/19.
 */
open class LoginContainerFragment: Fragment() {

    protected lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login_container,container,false)
        setViewModel()
        setListeners(view)
        setupScreen(viewModel.mode)
        return view
    }

    open fun setViewModel() {
        viewModel = LoginViewModel
        viewModel.initialize(activity!!.filesDir.absolutePath)
    }

    open fun setListeners(view:View) {
        viewModel.modeObserver.observe(this, Observer { loginMode -> setupScreen(loginMode)})
    }

    private fun setupScreen(loginMode:LoginMode) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        when (loginMode) {
            LoginMode.LOGIN -> transaction.replace(R.id.fragment_login_container,
                LoginFragment()
            )
            LoginMode.REGISTER -> transaction.replace(R.id.fragment_login_container,
                RegisterFragment()
            )
        }
        transaction.commit()
    }

}
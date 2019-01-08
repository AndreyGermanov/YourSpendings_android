package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

@SuppressLint("ValidFragment")

/**
 * Created by Andrey Germanov on 1/8/19.
 */
open class HeaderFragment(open var fragmentId:Int = R.layout.fragment_header): Fragment() {

    protected open lateinit var viewModel: ViewModel
    protected lateinit var menuButton: ImageButton
    protected lateinit var headerTitle: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(fragmentId,container,false)
        setViewModel()
        bindUI(view)
        setListeners(view)
        return view
    }

    open fun setViewModel() { }

    open fun bindUI(view: View) {
        headerTitle = view.findViewById(R.id.header_title)
        menuButton = view.findViewById(R.id.drawer_menu)
    }

    open fun setListeners(view: View) {
        menuButton.setOnClickListener { (activity as MainActivity).drawer.openDrawer(GravityCompat.START,true)}
    }

}
package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.services.LocationManager
import andrey.ru.yourspendings.views.viewmodels.EntityViewModel
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
@SuppressLint("ValidFragment")

/**
 * Created by Andrey Germanov on 1/9/19.
 */
open class EntityScreenFragment<T: Model>(open var fragmentId:Int,open var className:String): Fragment() {
    protected lateinit var viewModel: EntityViewModel<T>
    protected var currentItemId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(fragmentId,container,false)
        LocationManager.setup(this.activity!!)
        setViewModel()
        bindUI(view)
        setListeners(view)
        return view
    }
    open fun setViewModel() {
        viewModel = EntityViewModel.getViewModel(this,className)!!
        viewModel.initialize()
        currentItemId = viewModel.getCurrentItemId().value ?: ""
    }

    open fun bindUI(view: View) {}

    open fun setListeners(view: View) {
        viewModel.getScreenMode().observe(this, Observer { switchScreen(view, it) })
    }

    private fun switchScreen(view: View, mode: ScreenMode) {
        if (view.findViewById<FrameLayout>(R.id.fragment_container) == null) return
        when (mode) {
            ScreenMode.LIST -> {
                val list = PlacesListFragment()
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,list).commit()
            }
            ScreenMode.ITEM -> {
                val item = PlaceFragment()
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,item).commit()
            }
        }
    }
}
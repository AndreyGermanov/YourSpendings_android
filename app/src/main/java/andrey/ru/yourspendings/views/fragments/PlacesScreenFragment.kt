package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer

/**
 * Created by Andrey Germanov on 1/7/19.
 */
class PlacesScreenFragment: PlaceFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_place_activity,container,false)
        setViewModel()
        setEventListeners(view)
        viewModel.setLandscape(view.findViewById<FrameLayout>(R.id.fragment_container) == null)
        return view
    }

    private fun setEventListeners(view:View) =
        viewModel.getScreenMode().observe(this, Observer {switchScreen(view,it)})

    private fun switchScreen(view:View,mode: ScreenMode) {
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
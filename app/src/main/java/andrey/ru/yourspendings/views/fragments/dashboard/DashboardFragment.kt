package andrey.ru.yourspendings.views.fragments.dashboard

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.fragments.ModelHeaderFragment
import andrey.ru.yourspendings.views.viewmodels.MainViewModel
import andrey.ru.yourspendings.views.viewmodels.Screens
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

/**
 * Created by Andrey Germanov on 1/8/19.
 */
class DashboardFragment: Fragment() {
    private lateinit var viewModel:MainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard,container,false)
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.header_fragment, ModelHeaderFragment<Model>().apply{fragmentId = R.layout.fragment_header})
            .commit()
        setViewModel()
        setListeners(view)
        return view
    }

    private fun setViewModel() {
        viewModel = activity.run { ViewModelProviders.of(activity!!).get(MainViewModel::class.java)}
    }

    private fun setListeners(view:View) {
        with(view) {
            findViewById<Button>(R.id.new_purchase_button).setOnClickListener { viewModel.setScreen(Screens.NEW_PURCHASE)}
            findViewById<Button>(R.id.purchases_list_button).setOnClickListener { viewModel.setScreen(Screens.PURCHASES)}
            findViewById<Button>(R.id.places_list_button).setOnClickListener { viewModel.setScreen(Screens.PLACES)}
            findViewById<Button>(R.id.logout_button).setOnClickListener {viewModel.logout()}
        }
    }

}
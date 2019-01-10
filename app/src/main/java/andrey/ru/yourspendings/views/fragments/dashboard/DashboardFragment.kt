package andrey.ru.yourspendings.views.fragments.dashboard

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.fragments.ModelHeaderFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by Andrey Germanov on 1/8/19.
 */
class DashboardFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard,container,false)
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.header_fragment, ModelHeaderFragment<Model>().apply{fragmentId = R.layout.fragment_header})
            .commit()
        return view
    }

}
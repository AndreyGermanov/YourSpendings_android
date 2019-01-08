package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
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
        return view
    }

}
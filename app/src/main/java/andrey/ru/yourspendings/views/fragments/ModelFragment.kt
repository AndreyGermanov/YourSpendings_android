package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.services.LocationManager
import andrey.ru.yourspendings.views.viewmodels.EntityViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by Andrey Germanov on 1/10/19.
 */
abstract class ModelFragment<T: Model>: Fragment() {

    open var fragmentId: Int = 0
    open var className:String = ""

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
        if (className.isNotEmpty()) {
            viewModel = EntityViewModel.getViewModel(this.activity!!, className)!!
            viewModel.initialize()
            currentItemId = viewModel.getCurrentItemId().value ?: ""
        }
    }

    abstract fun bindUI(view: View)

    abstract fun setListeners(view: View)
}
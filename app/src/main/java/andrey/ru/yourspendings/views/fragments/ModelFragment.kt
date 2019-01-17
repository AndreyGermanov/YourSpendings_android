package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.services.LocationManager
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.viewmodels.ActivityEvent
import andrey.ru.yourspendings.views.viewmodels.ActivityEventSubscriber
import andrey.ru.yourspendings.views.viewmodels.EntityViewModel
import andrey.ru.yourspendings.views.viewmodels.MainViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by Andrey Germanov on 1/10/19.
 */
abstract class ModelFragment<T: Model>: Fragment(),ActivityEventSubscriber {

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
        (activity as? MainActivity)?.apply {
            this.viewModel = MainViewModel
            subscribe(this@ModelFragment)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.unsubscribe(this)
    }

    open fun setViewModel() {
        if (className.isNotEmpty()) {
            viewModel = EntityViewModel.getViewModel(className,arguments?.getBoolean("selectMode") ?: false)!!
            viewModel.initialize(activity!!.filesDir.absolutePath)
            currentItemId = viewModel.currentItemId
            if (currentItemId.isEmpty()) {
                currentItemId = arguments?.getString("currentItemId") ?: ""
                viewModel.currentItemId = currentItemId
            }
        }
    }

    abstract fun bindUI(view: View)

    abstract fun setListeners(view: View)

    override fun onActivityEvent(event: ActivityEvent) {}
}
package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.fragments.places.PlaceFragment
import andrey.ru.yourspendings.views.fragments.places.PlacesHeaderFragment
import andrey.ru.yourspendings.views.fragments.places.PlacesListFragment
import andrey.ru.yourspendings.views.fragments.purchases.PurchaseFragment
import andrey.ru.yourspendings.views.fragments.purchases.PurchasesHeaderFragment
import andrey.ru.yourspendings.views.fragments.purchases.PurchasesListFragment
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Observer

/**
 * Created by Andrey Germanov on 1/9/19.
 */
@Suppress("UNCHECKED_CAST")
open class ModelScreenFragment<T: Model>: ModelFragment<T>() {

    override var fragmentId:Int = R.layout.fragment_model_activity
    override var className:String = ""

    override fun bindUI(view: View) {
        if (arguments?.getBoolean("newItem") == true) {
            viewModel.screenMode = ScreenMode.ITEM
            viewModel.currentItemId = "new"
        } else if (arguments?.getBoolean("selectMode") == true) {
            viewModel.screenMode = ScreenMode.LIST
        }
        switchScreen(view,viewModel.screenMode)
    }

    override fun setListeners(view: View) {
        if (className.isNotEmpty())
            viewModel.screenModeObserver.observe(this, Observer
            {
                switchScreen(view, it)
            })
    }

    private fun switchScreen(view: View, mode: ScreenMode) {
        val header = getHeaderFragment()
        header.arguments = arguments
        val list = getListFragment()
        list.arguments = arguments
        val item = getItemFragment()
        item.arguments = arguments
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.header_fragment,header)
        if (view.findViewById<FrameLayout>(R.id.list_fragment) != null) {
            transaction.replace(R.id.list_fragment,list).replace(R.id.item_fragment,item).commit()
            viewModel.isLandscape = true
            return
        }
        when (mode) {
            ScreenMode.LIST -> {
                transaction.replace(R.id.fragment_container,list).commit()
            }
            ScreenMode.ITEM -> {
                transaction.replace(R.id.fragment_container,item).commit()
            }
        }
        viewModel.isLandscape = false
    }

    private fun getListFragment():ModelListFragment<T> {
        return (when(className) {
            "Place" -> PlacesListFragment()
            "Purchase" -> PurchasesListFragment()
            else -> null
        }) as ModelListFragment<T>
    }

    private fun getItemFragment():ModelItemFragment<T> {
        return (when(className) {
            "Place" -> PlaceFragment()
            "Purchase" -> PurchaseFragment()
            else -> null
        }) as ModelItemFragment<T>
    }

    private fun getHeaderFragment():ModelHeaderFragment<T> {
        return (when(className) {
            "Place" -> PlacesHeaderFragment()
            "Purchase" -> PurchasesHeaderFragment()
            else -> null
        }) as ModelHeaderFragment<T>
    }

}
package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import andrey.ru.yourspendings.views.viewmodels.PlacesViewModel
import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

@SuppressLint("ValidFragment")

/**
 * Created by Andrey Germanov on 1/5/19.
 */
class PlacesHeaderFragment(override var fragmentId:Int = R.layout.fragment_places_header): HeaderFragment() {

    private lateinit var backButton: ImageButton
    private lateinit var addButton: ImageButton

    override fun setViewModel() {
        viewModel = activity?.run { ViewModelProviders.of(this).get(PlacesViewModel::class.java) }
                ?: throw Exception("Invalid Activity")
    }

    override fun bindUI(view:View) {
        super.bindUI(view)
        addButton = view.findViewById(R.id.add_button)
        backButton = view.findViewById(R.id.back_button)
    }

    override fun setListeners(view:View) {
        super.setListeners(view)
        val viewModel = viewModel as? PlacesViewModel ?: return
        viewModel.getScreenMode().observe(this, Observer { mode ->
            switchHeaderMode(mode,viewModel.isLandscapeMode())
        })
        viewModel.getLandscape().observe(this, Observer { isLandscape ->
            switchHeaderMode(viewModel.getScreenMode().value!!,isLandscape)
        })
        backButton.setOnClickListener{ viewModel.setScreenMode(ScreenMode.LIST) }
        addButton.setOnClickListener {
            viewModel.clearFields()
            viewModel.setCurrentItemId("new")
            viewModel.setScreenMode(ScreenMode.ITEM)
        }
    }

    private fun switchHeaderMode(mode: ScreenMode, isLandscape:Boolean) {
        val viewModel = viewModel as? PlacesViewModel ?: return
        if (isLandscape) {
            backButton.visibility = View.GONE
            headerTitle.text = getString(R.string.places_list)
            addButton.visibility = View.VISIBLE
            return
        }
        when (mode) {
            ScreenMode.LIST -> {
                backButton.visibility = View.GONE
                addButton.visibility = View.VISIBLE
                headerTitle.text = getString(R.string.places_list)
            }
            ScreenMode.ITEM -> {
                headerTitle.text = viewModel.getCurrentItem()?.name ?: ""
                backButton.visibility = View.VISIBLE
                addButton.visibility = View.GONE
            }
        }
    }
}
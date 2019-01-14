package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer

/**
 * Created by Andrey Germanov on 1/9/19.
 */
open class ModelHeaderFragment<T: Model>: ModelFragment<T>() {

    override var fragmentId:Int=R.layout.fragment_model_header
    override var className:String = ""

    private lateinit var menuButton: ImageButton
    private lateinit var headerTitle: TextView
    private lateinit var backButton: ImageButton
    private lateinit var addButton: ImageButton

    override fun bindUI(view: View) {
        with(view) {
            headerTitle = findViewById(R.id.header_title)
            menuButton = findViewById(R.id.drawer_menu)
            if (!className.isEmpty()) {
                addButton = findViewById(R.id.add_button)
                backButton = findViewById(R.id.back_button)
            } else headerTitle.text = getString(R.string.dashboard)
        }
    }

    override fun setListeners(view: View) {

        menuButton.setOnClickListener { (activity as MainActivity).drawer.openDrawer(GravityCompat.START,true)}

        if (!className.isEmpty()) {
            viewModel.getScreenMode().observe(this, Observer { mode ->
                switchHeaderMode(mode, viewModel.isLandscapeMode())
            })
            viewModel.getLandscape().observe(this, Observer { isLandscape ->
                switchHeaderMode(viewModel.getScreenMode().value!!, isLandscape)
            })
            backButton.setOnClickListener { viewModel.setScreenMode(ScreenMode.LIST) }
            addButton.setOnClickListener {
                viewModel.clearFields()
                viewModel.setCurrentItemId("new")
                viewModel.setScreenMode(ScreenMode.ITEM)
            }
        }
    }

    private fun switchHeaderMode(mode: ScreenMode, isLandscape:Boolean) {
        if (isLandscape) {
            backButton.visibility = View.GONE
            addButton.visibility = View.VISIBLE
            headerTitle.text = viewModel.getListTitle()
            return
        }
        when (mode) {
            ScreenMode.LIST -> {
                backButton.visibility = View.GONE
                addButton.visibility = View.VISIBLE
                headerTitle.text = viewModel.getListTitle()
            }
            ScreenMode.ITEM -> {
                backButton.visibility = View.VISIBLE
                addButton.visibility = View.GONE
                headerTitle.text = viewModel.getCurrentItem()?.getTitle() ?: ""
            }
        }
    }

}
package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import android.app.AlertDialog
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer

/**
 * Created by Andrey Germanov on 1/9/19.
 */
open class ModelItemFragment<T: Model>: ModelFragment<T>(), View.OnKeyListener {

    override var fragmentId:Int = 0
    override var className:String = ""

    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button

    override fun bindUI(view: View) {
        view.visibility = View.INVISIBLE
        saveButton = view.findViewById(R.id.save_place_btn)
        deleteButton = view.findViewById(R.id.delete_place_btn)
        setFields()
    }

    override fun setListeners(view: View) {
        viewModel.getCurrentItemId().observe(this, Observer<String> { id ->
            currentItemId = id
            prepareItemForm(view)
        })

        saveButton.setOnClickListener { saveItem() }

        deleteButton.setOnClickListener {
            AlertDialog.Builder(this.context).apply {
                setMessage(getString(R.string.are_you_sure))
                setPositiveButton(getString(R.string.yes)) { _, _ -> deleteItem() }
                setNegativeButton(getString(R.string.no)) { _, _ -> }
            }.create().show()
        }
    }

    private fun prepareItemForm(view: View) {
        bindUI(view)
        val item = viewModel.getItems().value?.find { it.id == currentItemId }
        if (currentItemId.isNotEmpty()) view.visibility = View.VISIBLE; else view.visibility = View.INVISIBLE
        if (currentItemId == "new") {
            deleteButton.visibility = View.GONE
            setFields()
        } else {
            deleteButton.visibility = View.VISIBLE
            if (viewModel.getFields()["id"] != item?.id) {
                setFields(item?.toHashMap())
                viewModel.setFields(getFields())
            }
        }
    }

    private fun saveItem() {
        viewModel.saveChanges(getFields()) {error ->
            if (error != null) Toast.makeText(this.context,error, Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteItem() {
        viewModel.deleteItem { error ->
            if (error != null) Toast.makeText(this.context,error, Toast.LENGTH_LONG).show()
            viewModel.clearFields()
            viewModel.setCurrentItemId("")
            viewModel.setScreenMode(ScreenMode.LIST)
        }

    }

    open fun getFields():HashMap<String,Any> = HashMap()

    open fun setFields(fields:Map<String,Any>?=null) {}

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        viewModel.setFields(getFields())
        return false
    }
}
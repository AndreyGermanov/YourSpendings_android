package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Model
import andrey.ru.yourspendings.views.viewmodels.ScreenMode
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer

/**
 * Created by Andrey Germanov on 1/9/19.
 */
open class ModelItemFragment<T: Model>: ModelFragment<T>(), TextWatcher {

    override var fragmentId:Int = 0
    override var className:String = ""

    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button

    override fun bindUI(view: View) {
        with(view) {
            visibility = View.INVISIBLE
            saveButton = findViewById(R.id.save_place_btn)
            deleteButton = findViewById(R.id.delete_place_btn)
        }
        prepareItemForm(view)
    }

    override fun setListeners(view: View) {
        viewModel.currentItemIdObserver.observe(this, Observer<String> { id ->
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

    open fun prepareItemForm(view: View) {
        currentItemId = viewModel.currentItemId
        setFields(viewModel.fields)
        val item = viewModel.items.find { it.id == currentItemId }
        if (currentItemId.isNotEmpty()) view.visibility = View.VISIBLE; else view.visibility = View.INVISIBLE
        if (currentItemId == "new") {
            deleteButton.visibility = View.GONE
            setFields()
        } else {
            deleteButton.visibility = View.VISIBLE
            if (viewModel.fields["id"] != item?.id) {
                setFields(item?.toHashMap())
                viewModel.fields = getFields()
            }
        }
    }

    private fun saveItem() {
        viewModel.saveChanges(getFields()) { error ->
            if (this.context!=null && error!=null) Toast.makeText(this.context,error, Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteItem() {
        viewModel.deleteItem { error ->
            if (error != null && this.context!=null) Toast.makeText(this.context,error, Toast.LENGTH_LONG).show()
            else with (viewModel) { clearFields(); currentItemId=""; screenMode=ScreenMode.LIST }
        }
    }

    open fun getFields():HashMap<String,Any> = HashMap()

    open fun setFields(fields:Map<String,Any>?=null) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) { viewModel.fields = getFields() }
}
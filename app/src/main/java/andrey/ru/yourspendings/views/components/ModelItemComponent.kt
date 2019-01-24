package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.Button

/**
 * Created by Andrey Germanov on 1/21/19.
 */
@SuppressLint("ViewConstructor")
open class ModelItemComponent(open val context:MainActivity):Component(context) {

    lateinit var saveButton: Button
    lateinit var deleteButton: Button
    lateinit var formContainer: ViewGroup

    override fun bindUI() {
        saveButton = findViewById(R.id.save_place_btn)
        deleteButton = findViewById(R.id.delete_place_btn)
        formContainer = findViewById(R.id.form_container)
    }
}
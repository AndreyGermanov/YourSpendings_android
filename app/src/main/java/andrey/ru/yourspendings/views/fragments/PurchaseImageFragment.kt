package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import java.io.File

/**
 * Created by Andrey Germanov on 1/13/19.
 */
@Suppress("MemberVisibilityCanBePrivate")
class PurchaseImageFragment: Fragment() {

    lateinit var imageView: ImageView
    lateinit var exitButton:Button
    lateinit var deleteButton:Button
    var imagePath:String = ""
    var subscriberId:String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_purchase_image, container, false)
        imagePath = arguments?.getString("imagePath") ?: ""
        bindUI(view)
        setListeners()
        return view

    }

    fun bindUI(view:View) {
        with(view) {
            imageView = findViewById(R.id.purchase_image)
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath))
            exitButton = findViewById(R.id.exit_button)
            deleteButton = findViewById(R.id.delete_button)
        }
    }

    fun setListeners() {
        val state = (activity as MainActivity).store.state.purchasesState
        exitButton.setOnClickListener { state.imagesPagerOpened = false }
        deleteButton.setOnClickListener {
            state.currentImageId = File(imagePath).nameWithoutExtension
            state.removeImageSelected = true
            state.imagesPagerOpened = false
        }
    }
}
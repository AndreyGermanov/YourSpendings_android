package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.ui.NavigableImageView
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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
    private val fullScreen = LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)
    private val horizontal = LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT)
    private val wrap = LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        LinearLayout(activity).apply { layoutParams = fullScreen
        orientation = LinearLayout.VERTICAL
        addView(NavigableImageView(activity!!). apply {
            layoutParams = fullScreen.apply { weight = 10.0f }
        }.also {
            imagePath = arguments?.getString("imagePath") ?: ""
            imageView = it
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath))
        })
        addView(LinearLayout(activity).apply {layoutParams = horizontal
            orientation = LinearLayout.HORIZONTAL
            addView(button(activity!!.resources.getString(R.string.delete)).also { deleteButton = it})
            addView(button(activity!!.resources.getString(R.string.exit)).also { exitButton = it })
        })
    }.also { setListeners() }


    fun setListeners() {
        val state = (activity as MainActivity).store.state.purchasesState
        exitButton.setOnClickListener { state.imagesPagerOpened = false }
        deleteButton.setOnClickListener {
            state.currentImageId = File(imagePath).nameWithoutExtension
            state.removeImageSelected = true
            state.imagesPagerOpened = false
        }
    }

    fun button(title:String) = Button(activity).apply { layoutParams = wrap.apply{ weight = 1.0f}; text = title }
}
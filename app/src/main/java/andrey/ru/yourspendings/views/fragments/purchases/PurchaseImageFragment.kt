package andrey.ru.yourspendings.views.fragments.purchases

import andrey.ru.yourspendings.R
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

/**
 * Created by Andrey Germanov on 1/13/19.
 */
class PurchaseImageFragment: Fragment() {

    lateinit var imageView: ImageView
    lateinit var exitButton:Button
    lateinit var deleteButton:Button
    lateinit var imagePath:String
    lateinit var subscriberId:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_purchase_image, container, false)
        bindUI(view)
        setListeners()
        return view
    }

    fun bindUI(view:View) {
        imageView = view.findViewById(R.id.purchase_image)
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath))
        exitButton = view.findViewById(R.id.exit_button)
        deleteButton = view.findViewById(R.id.delete_button)
    }

    fun setListeners() {
        exitButton.setOnClickListener {
            activity!!.finish()
        }
        deleteButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("imagePath",imagePath)
            intent.putExtra("subscriberId",subscriberId)
            activity!!.setResult(RESULT_OK,intent)
            activity!!.finish()
        }
    }

}
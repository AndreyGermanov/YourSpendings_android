package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.adapters.PurchaseImagesPagerAdapter
import andrey.ru.yourspendings.views.store.PurchasesState
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager

/**
 * Created by Andrey Germanov on 1/23/19.
 */
class PurchaseImagesPagerFragment: DialogFragment() {

    lateinit var state: PurchasesState
    lateinit var pager: ViewPager

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val activity = activity as MainActivity
        state = activity.store.state.purchasesState
        val view = inflater.inflate(R.layout.activity_purchase_image,null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI(view)
        setListeners(view)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        state.imagesPagerOpened = false
    }

    private fun setupUI(view:View) {
        pager = view.findViewById(R.id.pager)
        pager.adapter = PurchaseImagesPagerAdapter(childFragmentManager,state)
        pager.currentItem = state.images.keys.toSortedSet().indexOf(state.currentImageId)
    }

    private fun setListeners(view: View) {
        pager.addOnPageChangeListener(OnPagerListener(state))
    }

}

class OnPagerListener(val state:PurchasesState): ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        state.currentImageId = state.imagesList.elementAt(position)
    }

}
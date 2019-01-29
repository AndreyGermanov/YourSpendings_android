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
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager

/**
 * Created by Andrey Germanov on 1/23/19.
 */
class PurchaseImagesPagerFragment: FullScreenFragment() {

    lateinit var state: PurchasesState
    private lateinit var pager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        ViewPager(activity!!).apply {
            val activity = activity as MainActivity
            state = activity.store.state.purchasesState
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)
            adapter =  PurchaseImagesPagerAdapter(childFragmentManager,state)
            currentItem = state.images.keys.toSortedSet().indexOf(state.currentImageId)
            id = R.id.view_pager
        }.also { pager = it; setListeners()}

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        state.imagesPagerOpened = false
    }

    private fun setListeners() {
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
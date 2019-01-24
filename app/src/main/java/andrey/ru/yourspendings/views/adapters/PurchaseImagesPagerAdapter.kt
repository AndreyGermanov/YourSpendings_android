package andrey.ru.yourspendings.views.adapters

import andrey.ru.yourspendings.views.fragments.PurchaseImageFragment
import andrey.ru.yourspendings.views.store.PurchasesState
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Andrey Germanov on 1/13/19.
 */
class PurchaseImagesPagerAdapter(fm:FragmentManager,val state: PurchasesState):FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = state.images.size

    override fun getItem(position: Int): Fragment =
        PurchaseImageFragment().apply {
            arguments = Bundle().apply {
                putString("imagePath",
                    state.imgCachePath+"/"+state.currentItemId+"/"+state.imagesList.elementAt(position)+".jpg")
            }
        }
}
package andrey.ru.yourspendings.views.adapters

import andrey.ru.yourspendings.views.fragments.purchases.PurchaseImageFragment
import andrey.ru.yourspendings.views.viewmodels.PurchaseImageViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Andrey Germanov on 1/13/19.
 */
class PurchaseImagesPagerAdapter(fm:FragmentManager,val viewModel:PurchaseImageViewModel):FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = viewModel.images.size

    override fun getItem(position: Int): Fragment =
        PurchaseImageFragment().apply {
            arguments = Bundle().apply {
                putString("imagePath",viewModel.images[position])
                putString("subscriberId",viewModel.subscriberId)
            }
            subscriberId = viewModel.subscriberId
        }
}
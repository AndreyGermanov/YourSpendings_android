package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.adapters.PurchaseImagesPagerAdapter
import andrey.ru.yourspendings.views.viewmodels.PurchaseImageViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager

/**
 * Created by Andrey Germanov on 1/13/19.
 */
@Suppress("UNCHECKED_CAST")
class PurchaseImageActivity: AppCompatActivity() {

    private lateinit var viewModel:PurchaseImageViewModel
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_image)
        setViewModel()
        bindUI()
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this).get(PurchaseImageViewModel::class.java)
        if (viewModel.currentImageId == -1) viewModel.currentImageId = intent.getIntExtra("currentItemId",0)
        if (viewModel.subscriberId.isEmpty()) viewModel.subscriberId = intent.getStringExtra("subscriberId")
        viewModel.images = intent.getSerializableExtra("images") as? ArrayList<String> ?: ArrayList()
    }

    private fun bindUI() {
        viewPager = findViewById(R.id.pager)
        viewPager.adapter = PurchaseImagesPagerAdapter(supportFragmentManager,viewModel)
        viewPager.setCurrentItem(viewModel.currentImageId,true)
    }
}
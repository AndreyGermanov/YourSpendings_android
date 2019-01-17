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

        viewModel = PurchaseImageViewModel.apply {
            initialize(filesDir.absolutePath)
            subscriberId = intent.getStringExtra("subscriberId")
            currentImageId = intent.getIntExtra("currentItemId",0)
            images = intent.getSerializableExtra("images") as? ArrayList<String> ?: ArrayList()
        }
    }

    private fun bindUI() {
        viewPager = findViewById<ViewPager>(R.id.pager).apply {
            adapter = PurchaseImagesPagerAdapter(supportFragmentManager,viewModel)
            setCurrentItem(viewModel.currentImageId,true)
        }
    }
}
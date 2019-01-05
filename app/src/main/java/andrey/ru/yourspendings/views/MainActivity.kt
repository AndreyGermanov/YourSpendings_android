package andrey.ru.yourspendings.views

import andrey.ru.yourspendings.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel:PlacesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewModel()
        setEventListeners()
    }

    private fun setViewModel() { viewModel = ViewModelProviders.of(this).get(PlacesViewModel::class.java) }

    private fun setEventListeners() = viewModel.getPlacesScreenMode().observe(this, Observer {switchScreen(it)})

    private fun switchScreen(mode:PlacesScreenMode) {
        when (mode) {
            PlacesScreenMode.LIST -> {
                val list = PlacesListFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,list).commit()
            }
            PlacesScreenMode.ITEM -> {
                val item = PlaceFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,item).commit()
            }
        }
    }
}

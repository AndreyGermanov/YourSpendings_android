package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import com.google.android.gms.maps.MapView

/**
 * Created by Andrey Germanov on 1/31/19.
 */
@SuppressLint("ViewConstructor")
class MapComponent(context: MainActivity): Component(context) {

    var mapView: MapView? = null

    override fun render() {
        addView(MapView(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)
        }.also { mapView = it })
    }
}
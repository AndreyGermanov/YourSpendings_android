package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Place
import android.annotation.SuppressLint
@SuppressLint("ValidFragment")

/**
 * Created by Andrey Germanov on 1/5/19.
 */
class PlacesListFragment(override var fragmentId:Int = R.layout.fragment_places,
                         override var className:String = Place.getClassName())
    : EntityListFragment<Place>(fragmentId,className)
package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.Place
import android.annotation.SuppressLint
@SuppressLint("ValidFragment")

/**
 * Created by Andrey Germanov on 1/7/19.
 */
open class PlacesScreenFragment (
    override var fragmentId:Int = R.layout.fragment_place_activity,
    override var className:String = Place.getClassName()): EntityScreenFragment<Place>(fragmentId,className)
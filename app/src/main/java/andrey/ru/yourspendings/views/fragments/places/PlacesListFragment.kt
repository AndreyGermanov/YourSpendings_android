package andrey.ru.yourspendings.views.fragments.places

import andrey.ru.yourspendings.models.Place
import andrey.ru.yourspendings.views.fragments.ModelListFragment

/**
 * Created by Andrey Germanov on 1/5/19.
 */
class PlacesListFragment: ModelListFragment<Place>() {override var className:String = Place.getClassName()}
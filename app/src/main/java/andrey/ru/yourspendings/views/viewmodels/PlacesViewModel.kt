package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.IDataCollection
import andrey.ru.yourspendings.models.Place
import andrey.ru.yourspendings.models.PlacesCollection

/**
 * Created by Andrey Germanov on 1/5/19.
 */
object PlacesViewModel: EntityViewModel<Place>(Collection = PlacesCollection)
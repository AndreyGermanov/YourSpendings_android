package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.store.ModelState

/**
 * Created by Andrey Germanov on 1/21/19.
 */
class PlacesListContainer:ModelListContainer() {

    override fun initialize(context: MainActivity,modelState: ModelState?) {
        this.state = modelState ?: context.store.state.placesState
        super.initialize(context,modelState)
    }
}
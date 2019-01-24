package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.store.ModelState

/**
 * Created by Andrey Germanov on 1/21/19.
 */
@Suppress("UNCHECKED_CAST")
class PlacesScreenContainer:ModelScreenContainer() {

    override fun initialize(context: MainActivity,modelState: ModelState?) {
        this.modelState = modelState ?: context.store.state.placesState
        listContainerClass = PlacesListContainer::class.java as Class<Container>
        itemContainerClass = PlaceContainer::class.java as Class<Container>
        super.initialize(context)
    }

}
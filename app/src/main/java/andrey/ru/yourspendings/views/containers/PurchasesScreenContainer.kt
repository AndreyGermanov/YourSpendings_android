package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.views.MainActivity

/**
 * Created by Andrey Germanov on 1/21/19.
 */
@Suppress("UNCHECKED_CAST")
class PurchasesScreenContainer:ModelScreenContainer() {

    override fun initialize(context: MainActivity) {
        modelState = context.store.state.purchasesState
        listContainerClass = PurchasesListContainer::class.java as Class<Container>
        itemContainerClass = PurchaseContainer::class.java as Class<Container>
        super.initialize(context)
    }
}
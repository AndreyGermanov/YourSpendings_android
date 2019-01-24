package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.views.MainActivity

/**
 * Created by Andrey Germanov on 1/21/19.
 */
class PurchasesListContainer:ModelListContainer() {

    override fun initialize(context: MainActivity) {
        state = context.store.state.purchasesState
        super.initialize(context)
    }
}
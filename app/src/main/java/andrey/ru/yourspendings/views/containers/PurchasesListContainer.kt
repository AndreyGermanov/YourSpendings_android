package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.models.*
import andrey.ru.yourspendings.views.MainActivity
import java.util.ArrayList

/**
 * Created by Andrey Germanov on 1/21/19.
 */
class PurchasesListContainer:ModelListContainer() {

    override fun initialize(context: MainActivity) {
        state = context.store.state.purchasesState
        super.initialize(context)
    }

    override fun subscribeToDB() {
        PlacesCollection.subscribe(this)
        PurchasesCollection.subscribe(this)
    }

    override fun onDataChange(items: ArrayList<Model>) {
        view.modelListAdapter.notifyDataSetChanged()
    }
}
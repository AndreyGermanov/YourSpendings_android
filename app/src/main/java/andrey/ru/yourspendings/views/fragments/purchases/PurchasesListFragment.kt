package andrey.ru.yourspendings.views.fragments.purchases

import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.views.fragments.ModelListFragment

/**
 * Created by Andrey Germanov on 1/9/19.
 */
class PurchasesListFragment: ModelListFragment<Purchase>() {override var className:String = Purchase.getClassName()}
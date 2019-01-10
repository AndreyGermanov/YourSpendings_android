package andrey.ru.yourspendings.views.fragments.purchases

import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.views.fragments.ModelScreenFragment

/**
 * Created by Andrey Germanov on 1/9/19.
 */
class PurchasesScreenFragment: ModelScreenFragment<Purchase>() {override var className:String = Purchase.getClassName()}

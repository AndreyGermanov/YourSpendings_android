package andrey.ru.yourspendings.views.fragments.purchases

import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.views.fragments.ModelHeaderFragment

/**
 * Created by Andrey Germanov on 1/10/19.
 */
class PurchasesHeaderFragment:ModelHeaderFragment<Purchase>() {override var className=Purchase.getClassName()}
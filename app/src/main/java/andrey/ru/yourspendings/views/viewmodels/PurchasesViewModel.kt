package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.IDataCollection
import andrey.ru.yourspendings.models.Purchase
import andrey.ru.yourspendings.models.PurchasesCollection

/**
 * Created by Andrey Germanov on 1/9/19.
 */
class PurchasesViewModel(override var Collection: IDataCollection<Purchase> = PurchasesCollection)
    : EntityViewModel<Purchase>(Collection)
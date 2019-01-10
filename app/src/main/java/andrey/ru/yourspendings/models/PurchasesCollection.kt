package andrey.ru.yourspendings.models

import java.util.*

/**
 * Created by Andrey Germanov on 1/9/19.
 */
object PurchasesCollection: Collection<Purchase>() {

    override val tableName
        get() = "purchases"

    override fun getCollectionName(): String = "purchases"

    override fun newItem(data:Map<String,Any>):Purchase = Purchase.fromHashMap(data)

    override fun validateItem(fields: HashMap<String, String>, callback: (result: Any) -> Unit) {
        val item = newItem(fields)
        if (item.id == "new") item.id = UUID.randomUUID().toString()
        callback(item)
    }

    override fun getListTitle() = "Purchases List"

}
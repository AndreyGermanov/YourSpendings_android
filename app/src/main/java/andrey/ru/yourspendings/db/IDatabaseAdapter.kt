package andrey.ru.yourspendings.db

import andrey.ru.yourspendings.models.IDatabaseSubscriber
import androidx.lifecycle.LiveData

/**
 * Created by Andrey Germanov on 1/4/19.
 */
interface IDatabaseAdapter {
    fun getList(collectionName:String,callback:(List<Map<String,Any>>) -> Unit)
    fun subscribe(subscriber: IDatabaseSubscriber)
    fun unsubscribe(subscriber: IDatabaseSubscriber)
}
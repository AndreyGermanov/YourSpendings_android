package andrey.ru.yourspendings.db

import andrey.ru.yourspendings.models.IDatabaseSubscriber

/**
 * Created by Andrey Germanov on 1/4/19.
 */
interface IDatabaseAdapter {
    fun getList(collectionName:String,callback:(List<Map<String,Any>>) -> Unit)
    fun saveItem(collectionName:String,data:HashMap<String,Any>,callback:(error:String?)->Unit)
    fun deleteItem(collectionName:String,id:String,callback:(error:String?)->Unit)
    fun subscribe(subscriber: IDatabaseSubscriber)
    fun unsubscribe(subscriber: IDatabaseSubscriber)
}
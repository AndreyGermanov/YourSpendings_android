package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/5/19.
 */
interface IDataCollection<T:Model> {
    fun subscribe(subscriber:IDataSubscriber<T>)
    fun unsubscribe(subscriber: IDataSubscriber<T>)
    fun saveItem(fields:HashMap<String,String>,callback:(result:Any)->Unit)
    fun deleteItem(id:String,callback:(error:String?)->Unit)
}
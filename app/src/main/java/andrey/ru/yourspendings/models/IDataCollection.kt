package andrey.ru.yourspendings.models

import android.content.Context

/**
 * Created by Andrey Germanov on 1/5/19.
 */
interface IDataCollection<T:Model> {
    fun subscribe(subscriber:IDataSubscriber<T>)
    fun unsubscribe(subscriber: IDataSubscriber<T>)
    fun saveItem(fields:HashMap<String,Any>,callback:(result:Any)->Unit)
    fun deleteItem(id:String,callback:(error:String?)->Unit)
    fun loadList(callback:(()->Unit)?=null)
    fun getList():ArrayList<T>
    fun getItemById(id:String):T?
    fun newItem(data:Map<String,Any>):T
    fun newItemFromDB(data:Map<String,Any>):T
    fun validateItem(fields:HashMap<String,Any>,callback:(result:Any)->Unit)
    fun getListTitle():String
    fun setPath(rootPath:String)
}
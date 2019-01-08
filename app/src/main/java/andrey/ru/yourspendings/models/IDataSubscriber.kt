package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/5/19.
 */
interface IDataSubscriber<T:Model> {
    fun onDataChange(items:ArrayList<T>)
}
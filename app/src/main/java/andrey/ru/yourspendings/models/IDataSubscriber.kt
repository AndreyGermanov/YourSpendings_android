package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/5/19.
 */
interface IDataSubscriber {
    fun onDataChange(items:ArrayList<Place>)
}
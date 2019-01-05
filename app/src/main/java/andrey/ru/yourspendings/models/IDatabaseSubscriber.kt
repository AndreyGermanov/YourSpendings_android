package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/4/19.
 */
interface IDatabaseSubscriber {
    fun getCollectionName():String
    fun onDataChange(changes:Map<String,List<Map<String,Any>>>)
}
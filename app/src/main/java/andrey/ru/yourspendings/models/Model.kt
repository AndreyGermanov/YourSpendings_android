package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/8/19.
 */
open class Model(open var id:String) {
    companion object {
        fun fromHashMap(data:Map<String,Any>):Model =
            Model(id = data["id"]?.toString() ?: "")
        fun fromHashMapOfDB(data:Map<String,Any>):Model = fromHashMap(data)
    }
    open fun toHashMap():HashMap<String,Any> = hashMapOf("id" to id)
    open fun toHashMapForDB():HashMap<String,Any> = toHashMap()
    open fun getTitle():String = ""
}
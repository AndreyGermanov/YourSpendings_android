package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/8/19.
 */
open class Model(open var id:String) {
    companion object {
        fun fromHashMap(data:Map<String,Any>):Model =
            Model(id = data["id"]?.toString() ?: "")
    }
}
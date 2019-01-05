package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/4/19.
 */
data class Place(
    var id:String,
    var name:String="",
    var latitude:Double = 0.0,
    var longitude:Double = 0.0
)

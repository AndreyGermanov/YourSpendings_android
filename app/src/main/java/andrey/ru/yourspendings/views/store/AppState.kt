package andrey.ru.yourspendings.views.store

import com.google.gson.Gson

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
class AppState(private val store:Store, val fields:MutableMap<String,Any>) {
    val gson = Gson()
    val mainState = MainState(this)
    val loginState = LoginState(this)

    fun writeFields(newFields:MutableMap<String,Any>) {
        val prevFields = gson.fromJson(gson.toJson(fields),HashMap::class.java) as HashMap<String,Any>
        val prevState = AppState(store,prevFields)
        newFields.toMap(fields)
        store.save(newFields)
        store.rootView.onStateChanged(this,prevState)
    }
}
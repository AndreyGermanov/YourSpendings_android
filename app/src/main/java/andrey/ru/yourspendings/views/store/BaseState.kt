package andrey.ru.yourspendings.views.store

import com.google.gson.Gson

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseState(open val state:AppState, open val index:String) {

    private val gson = Gson()

    open fun setValue(fieldName:String, fieldValue:Any) {
        val storeFields:HashMap<String,Any> = gson.fromJson(gson.toJson(state.fields),HashMap::class.java) as HashMap<String,Any>
        state.writeFields(applyValueToFields(storeFields,fieldName,fieldValue))
    }

    open fun getValue(fieldName:String): Any? {
        val fields = getFieldsCopy(state.fields[index])
        return fields[fieldName]
    }

    open fun applyValueToFields(storeFields:MutableMap<String,Any>,fieldName:String,fieldValue:Any):MutableMap<String,Any> {
        val fields = storeFields[index] as? MutableMap<String,Any> ?: HashMap()
        fields[fieldName] = fieldValue
        storeFields[index] = fields
        return storeFields
    }
    open fun getFieldsCopy(fields:Any?):MutableMap<String,Any> =
        gson.fromJson(gson.toJson(fields as? MutableMap<String,Any>),HashMap::class.java) as? MutableMap<String, Any> ?: HashMap()

}
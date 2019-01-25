package andrey.ru.yourspendings.views.store

import com.google.gson.Gson

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseState(open val state:AppState, open val index:String) {

    private val gson = Gson()

    open fun initialize() {
        state.fieldSettings[index] = HashMap()
    }

    open fun setValue(fieldName:String, fieldValue:Any) {
        val storeFields:HashMap<String,Any> = gson.fromJson(gson.toJson(state.fields),HashMap::class.java) as HashMap<String,Any>
        var fieldsToWrite = applyValueToFields(storeFields,fieldName,fieldValue)
        state.triggerStateChange(fieldsToWrite)
        if (shouldWriteFieldToDB(fieldName)) {
            fieldsToWrite = gson.fromJson(gson.toJson(state.fields),HashMap::class.java) as HashMap<String,Any>
            state.writeFields(fieldsToWrite)
        }
    }

    open fun getValue(fieldName:String): Any? {
        val fields = getFieldsCopy(state.fields[index])
        return fields[fieldName]
    }

    open fun getValueFromMap(mapName:String,fieldName:String): Any? {
        val map = getValue(mapName) as? MutableMap<String,Any> ?: return null
        return map[fieldName]
    }

    open fun setValueToMap(mapName:String,fieldName:String,fieldValue:Any) {
        val map = getValue(mapName) as? MutableMap<String,Any> ?: HashMap()
        map[fieldName] = fieldValue
        setValue(mapName,map)
    }

    open fun applyValueToFields(storeFields:MutableMap<String,Any>,fieldName:String,fieldValue:Any):MutableMap<String,Any> {
        val fields = storeFields[index] as? MutableMap<String,Any> ?: HashMap()
        fields[fieldName] = fieldValue
        storeFields[index] = fields
        return storeFields
    }
    open fun getFieldsCopy(fields:Any?):MutableMap<String,Any> =
        gson.fromJson(gson.toJson(fields as? MutableMap<String,Any>),HashMap::class.java) as? MutableMap<String, Any> ?: HashMap()


    open fun shouldWriteFieldToDB(fieldName:String):Boolean = state.shouldWriteFieldToDB(fieldName,index)

    fun getStringValue(fieldName:String,default:String?=null):String {
        return getValue(fieldName)?.toString() ?: default ?: ""
    }

    fun getDoubleValue(fieldName:String,default:Double?=null): Double {
        return getStringValue(fieldName).toDoubleOrNull() ?: default ?: 0.0
    }

    fun getIntValue(fieldName:String,default:Int?=null):Int {
        return getDoubleValue(fieldName,default?.toDouble()).toInt()
    }

    fun getBooleanValue(fieldName:String,default:Boolean?=null):Boolean {
        return getStringValue(fieldName,default?.toString()).toBoolean()
    }

}
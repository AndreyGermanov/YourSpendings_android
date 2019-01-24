package andrey.ru.yourspendings.views.store

import andrey.ru.yourspendings.models.Model

/**
 * Created by Andrey Germanov on 1/19/19.
 */
@Suppress("UNCHECKED_CAST")
open class ModelState(override val state:AppState, open val modelName:String): BaseState(state,modelName) {

    override fun initialize() {
        state.fieldSettings[modelName] = hashMapOf(
            "itemsUpdateCounter" to hashMapOf("transient" to true),
            "isLoading" to hashMapOf("transient" to true),
            "popupMessage" to hashMapOf("transient" to true)
        ) as HashMap<String,Any>
    }

    var mode:ModelScreenMode
        get() = ModelScreenMode.valueOf(getValue("mode")?.toString() ?: "LIST")
        set(value:ModelScreenMode) { setValue("mode",value.toString())}

    var itemsUpdateCounter:Int
        get() = getValue("itemsUpdateCounter")?.toString()?.toDoubleOrNull()?.toInt() ?: 0
        set(value:Int) = setValue("itemsUpdateCounter",value.toString())

    var currentItemId:String
        get() = getValue("currentItemId")?.toString() ?: ""
        set(value:String) = setValue("currentItemId",value)

    open val items:ArrayList<Model>
        get() = ArrayList()

    open val item:Model?
        get() = Model("")

    var isLoading:Boolean
        get() = getValue("isLoading")?.toString()?.toBoolean() ?: false
        set(value:Boolean) { setValue("isLoading",value.toString())}

    var popupMessage:String
        get() = getValue("popupMessage")?.toString() ?: ""
        set(value:String) { setValue("popupMessage",value)}

    var fields: MutableMap<String,Any>
        get() = getValue("fields") as? MutableMap<String, Any> ?: HashMap()
        set(value:MutableMap<String,Any>) { setValue("fields",value) }

    var selectMode: Boolean
        get() = getBooleanValue("selectMode")
        set(value) { setValue("selectMode",value)}

    var itemIsSelected: Boolean
        get() = getBooleanValue("itemIsSelected")
        set(value) { setValue("itemIsSelected",value)}

    open fun getModelState(state:AppState):ModelState? {
        return null
    }

    open fun fillFieldsFromItem() {}

    open fun getListTitle():String = ""

}

enum class ModelScreenMode { LIST,ITEM }
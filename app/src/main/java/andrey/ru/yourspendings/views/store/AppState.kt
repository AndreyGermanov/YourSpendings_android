package andrey.ru.yourspendings.views.store

import com.google.gson.Gson

/**
 * Created by Andrey Germanov on 1/18/19.
 */
@Suppress("UNCHECKED_CAST")
class AppState(private val store:Store, val fields:MutableMap<String,Any>) {

    private val gson = Gson()

    val fieldSettings = HashMap<String,HashMap<String,Any>>()

    lateinit var mainState:MainState
    lateinit var loginState:LoginState
    lateinit var placesState:PlacesState
    lateinit var purchasesState:PurchasesState
    lateinit var dateTimePickerState: DateTimePickerState
    lateinit var selectPlaceState: PlacesState

    fun initialize() {
        mainState = MainState(this).apply { initialize() }
        loginState = LoginState(this).apply { initialize() }
        placesState = PlacesState(this).apply { initialize() }
        purchasesState = PurchasesState(this).apply { initialize() }
        dateTimePickerState = DateTimePickerState(this).apply { initialize() }
        selectPlaceState = PlacesState(this,"SelectPlace").apply { initialize();}
    }

    fun writeFields(newFields:MutableMap<String,Any>) {
        store.save(newFields.mapValues { map ->
            val fields = map.value as MutableMap<String,Any>
            fields.filter { shouldWriteFieldToDB(it.key,map.key) }
        } as MutableMap<String, Any>)
    }

    private fun getPrevState():AppState {
        val prevFields = gson.fromJson(gson.toJson(fields),HashMap::class.java) as HashMap<String,Any>
        return AppState(store,prevFields).apply {
            initialize()
        }
    }

    fun triggerStateChange(newFields:MutableMap<String,Any>) {
        val prevState = getPrevState()
        newFields.toMap(fields)
        store.rootView.onStateChanged(this,prevState)
    }

    fun getModelState(appState:AppState?=null): ModelState? {
        val checkState = appState ?: this
        return when(checkState.mainState.screen) {
            Screen.PLACES -> checkState.placesState
            Screen.PURCHASES ->
                return if (!checkState.purchasesState.selectPlaceDialogOpened)
                    checkState.purchasesState
                else
                    checkState.selectPlaceState
            else -> null
        }
    }

    fun shouldWriteFieldToDB(fieldName:String,index:String):Boolean {
        val fields = this.fieldSettings[index] ?: HashMap()
        val fieldOpts = fields[fieldName] as? HashMap<String,Any> ?: HashMap()
        val result = fieldOpts["transient"]?.toString()?.toBoolean() ?: return true
        return !result
    }
}
package andrey.ru.yourspendings.views.store

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.extensions.dateFromAny
import andrey.ru.yourspendings.models.*
import android.net.Uri
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Andrey Germanov on 1/21/19.
 */
@Suppress("UNCHECKED_CAST", "HasPlatformType")
class PurchasesState(override val state:AppState):ModelState(state,"Purchase") {

    override fun initialize() {
        super.initialize()
        with (state.fieldSettings[modelName]!!) {
            put("imagesUpdateCounter", hashMapOf("transient" to true))
            put("imageCapturedFromLibrary", hashMapOf("transient" to true))
            put("removeImageSelected", hashMapOf("transient" to true))
        }
    }

    override val item: Purchase?
        get() = PurchasesCollection.getItemById(currentItemId)

    override val items:ArrayList<Model>
        get() = PurchasesCollection.getList().apply { sortByDescending { it.date } } as ArrayList<Model>

    val imgCachePath:String
        get() = PurchasesCollection.imgCachePath

    var date: LocalDateTime
        get() = dateFromAny(getValueFromMap("fields","date"))
        set(value) = setValueToMap("fields","date",
            value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))

    var place: Place?
        get() = PlacesCollection.getItemById(getValueFromMap("fields","place_id")?.toString() ?: "")
        set(value) { setValueToMap("fields","place_id",value?.id ?: "") }

    var images: MutableMap<String,String>
        get() = getValueFromMap("fields","images") as? MutableMap<String, String> ?: HashMap()
        set(value) { setValueToMap("fields","images",value)}

    val imagesList: List<String>
        get() = images.keys.sorted()

    var imagesUpdateCounter:Int
        get() = getValue("imagesUpdateCounter")?.toString()?.toDoubleOrNull()?.toInt() ?: 0
        set(value) { setValue("imagesUpdateCounter",value) }

    var dateTimePickerOpened:Boolean
        get() = getValue("dateTimePickerOpened")?.toString()?.toBoolean() ?: false
        set(value) { setValue("dateTimePickerOpened",value)}

    var imageUri:Uri
        get() = Uri.parse(getStringValue("imageUri"))
        set(value) { setValue("imageUri",value.toString()) }

    var imageCapturedFromLibrary:Boolean
        get() = getBooleanValue("imageCapturedFromLibrary")
        set(value) { setValue("imageCapturedFromLibrary",value)}

    var takePictureDialogOpened:Boolean
        get() = getBooleanValue("takePictureDialogOpened")
        set(value) { setValue("takePictureDialogOpened",value) }

    var takePictureCameraDialogOpened: Boolean
        get() = getBooleanValue("takePictureCameraDialogOpened")
        set(value) { setValue("takePictureCameraDialogOpened", value ) }

    var imagesPagerOpened: Boolean
        get() = getBooleanValue("imagesPagerOpened")
        set(value) { setValue("imagesPagerOpened",value)}

    var currentImageId: String
        get() = getStringValue("currentImageId")
        set(value) { setValue("currentImageId", value)}

    var removeImageSelected: Boolean
        get() = getBooleanValue("removeImageSelected")
        set(value) { setValue("removeImageSelected",value)}

    var selectPlaceDialogOpened: Boolean
        get() = getBooleanValue("selectPlaceDialogOpened")
        set(value) { setValue("selectPlaceDialogOpened",value) }

    override fun getModelState(state:AppState):ModelState? {
        return state.purchasesState
    }

    override fun getListTitle() = state.store.app.getString(R.string.purchases_list)

    override fun fillFieldsFromItem() {
        val item = item
        if (item != null) {
            date = item.date
            place = item.place
            images = item.images as? MutableMap<String, String> ?: HashMap()

        } else {
            date = LocalDateTime.now()
            place = null
            images = HashMap()
        }
    }
}
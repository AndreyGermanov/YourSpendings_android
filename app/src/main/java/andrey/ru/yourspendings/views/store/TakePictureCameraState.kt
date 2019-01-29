package andrey.ru.yourspendings.views.store

/**
 * Created by Andrey Germanov on 1/29/19.
 */
@Suppress("UNCHECKED_CAST")
class TakePictureCameraState(override val state:AppState): BaseState(state,"TakePictureCamera") {

    override fun initialize() {
        state.fieldSettings[index] = hashMapOf(
            "isConfirmed" to hashMapOf("transient" to true),
            "isPictureSubmitted" to hashMapOf("transient" to true)
        ) as HashMap<String,Any>
    }
    var isPictureTaken:Boolean
        get() = getBooleanValue("isPictureTaken")
        set(value) = setValue("isPictureTaken",value)

    var isPictureSubmitted:Boolean
        get() = getBooleanValue("isPictureSubmitted")
        set(value) = setValue("isPictureSubmitted",value)

    var isConfirmed:Boolean
        get() = getBooleanValue("isConfirmed")
        set(value) = setValue("isConfirmed",value)
}

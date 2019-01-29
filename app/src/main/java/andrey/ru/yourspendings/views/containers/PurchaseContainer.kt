package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.models.*
import andrey.ru.yourspendings.services.CameraService
import andrey.ru.yourspendings.services.LocationManager
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.PurchaseComponent
import andrey.ru.yourspendings.views.fragments.SelectPlaceFragment
import andrey.ru.yourspendings.views.fragments.DateTimePickerFragment
import andrey.ru.yourspendings.views.fragments.PurchaseImagesPagerFragment
import andrey.ru.yourspendings.views.fragments.TakePictureCameraFragment
import andrey.ru.yourspendings.views.store.*
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by Andrey Germanov on 1/22/19.
 */
@Suppress("NAME_SHADOWING")
class PurchaseContainer:ModelContainer() {

    private var imagesPagerFragment: PurchaseImagesPagerFragment? = null
    private var dateTimePickerFragment: DateTimePickerFragment? = null
    private var selectPlaceFragment: SelectPlaceFragment? = null
    private var takePictureCameraFragment: TakePictureCameraFragment? = null

    private lateinit var selectPlaceState:PlacesState

    override fun initialize(context: MainActivity) {
        state = context.store.state.purchasesState
        selectPlaceState = context.store.state.selectPlaceState
        selectPlaceState.selectMode = true
        view = PurchaseComponent(context)
        component = view
        addChild(Container.getInstance(context,DateTimePickerContainer::class.java))
        addChild(Container.getModelInstance(context,PlacesScreenContainer::class.java,selectPlaceState))
        addChild(Container.getInstance(context,TakePictureCameraContainer::class.java))
        super.initialize(context)
    }

    override fun subscribeToDB() {
        PlacesCollection.subscribe(this)
        PurchasesCollection.subscribe(this)
    }

    override fun loadData(callback:()->Unit) {
        PurchasesCollection.loadList { callback() }
    }

    override fun updateForm() {
        val state = state as PurchasesState
        val view = view as PurchaseComponent
        view.date.text = state.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        view.place.text = state.place?.name ?: ""
        updateDialogs(state)
        view.imagesAdapter.notifyDataSetChanged()
    }

    private fun updateDialogs(state:PurchasesState) {
        updateDatePickerDialog(state)
        updateTakePictureDialog(state)
        updateImagesPagerDialog(state)
        updateSelectPlaceDialog(state)
        updateTakePictureCameraDialog(state)
    }

    private fun updateDatePickerDialog(state:PurchasesState) {
        val ctx = context
        if (state.dateTimePickerOpened) {
            if (dateTimePickerFragment == null) dateTimePickerFragment = DateTimePickerFragment()
                .apply {

                show(ctx.supportFragmentManager,ctx.getString(R.string.select_date))
            }
        } else { removeFragment(dateTimePickerFragment); dateTimePickerFragment = null }
    }

    private fun updateTakePictureDialog(state:PurchasesState) {
        if (state.takePictureDialogOpened) {
            context.let { activity ->
                AlertDialog.Builder(activity).apply {
                    setTitle(R.string.imageSource)
                    setItems(R.array.imageSources) { _: DialogInterface, sourceId:Int ->
                        when (sourceId) {
                            ImageSource.CAMERA.code -> takePictureFromCamera(state)
                            ImageSource.PICTURE_LIBRARY.code -> takePictureFromLibrary(state)
                        }
                    }.setOnCancelListener {
                        state.takePictureDialogOpened = false
                    }
                }.create().show()
            }
        }
    }

    private fun updateImagesPagerDialog(state:PurchasesState) {
        val ctx = context
        if (state.imagesPagerOpened && state.images.containsKey(state.currentImageId)) {
            if (imagesPagerFragment == null)
                imagesPagerFragment = PurchaseImagesPagerFragment()
                    .apply { show(ctx.supportFragmentManager,ctx.getString(R.string.images)) }
        } else { removeFragment(imagesPagerFragment); imagesPagerFragment = null }
    }

    private fun updateTakePictureCameraDialog(state:PurchasesState) {
        val ctx = context
        if (state.takePictureCameraDialogOpened) {
            if (takePictureCameraFragment == null) {
                takePictureCameraFragment = TakePictureCameraFragment().apply {
                    show(ctx.supportFragmentManager, ctx.getString(R.string.take_a_picture))
                }
            }
        } else { removeFragment(takePictureCameraFragment); takePictureCameraFragment = null }
    }

    private fun updateSelectPlaceDialog(state:PurchasesState) {
        val ctx = context
        if (state.selectPlaceDialogOpened) {
            if (selectPlaceFragment == null) {
                selectPlaceFragment = SelectPlaceFragment().apply { show(ctx.supportFragmentManager, ctx.getString(R.string.select_place)) }
            }
        } else { removeFragment(selectPlaceFragment); selectPlaceFragment = null }
    }

    private fun removeFragment(fragment: Fragment?) {
        if (fragment != null) {
            val dialog = fragment as DialogFragment
            if (dialog.fragmentManager != null) dialog.dismiss()
        }
    }

    override fun setListeners() {
        super.setListeners()
        val state = state as PurchasesState
        val view = view as PurchaseComponent
        view.selectDateButton.setOnClickListener {
            with (context.store.state.dateTimePickerState) {
                year = state.date.year
                month = state.date.month.value-1
                day = state.date.dayOfMonth
                hour = state.date.hour
                minute = state.date.minute
                subscriberId = "Purchase_"+state.currentItemId
                confirmed = false
                dateSelected = false
            }
            state.dateTimePickerOpened = true
        }
        view.detectPlaceButton.setOnClickListener { detectPlace(state) }
        view.takePictureButton.setOnClickListener { state.takePictureDialogOpened = true }
        view.selectPlaceButton.setOnClickListener {
            state.selectPlaceDialogOpened = true
            selectPlaceState.itemIsSelected = false
            selectPlaceState.currentItemId = state.place?.id ?: ""
            selectPlaceState.mode = ModelScreenMode.LIST
        }
    }

    override fun save(callback: (result: Any) -> Unit) {
        PurchasesCollection.saveItem(state.fields.apply { put("id",state.currentItemId)}) {
                result -> callback(result)
        }
    }

    override fun delete(callback: (error: String?) -> Unit) {
        PurchasesCollection.deleteItem(state.currentItemId) { result -> callback(result) }
    }

    private fun takePictureFromCamera(state:PurchasesState) {
        state.takePictureDialogOpened = false
        with (context.store.state.takePictureCameraState) {
            isConfirmed = false
            isPictureSubmitted = false
        }
        state.takePictureCameraDialogOpened = true
        context.store.state.takePictureCameraState.isPictureTaken = false
    }

    private fun takePictureFromLibrary(state:PurchasesState) {
        state.imageCapturedFromLibrary = false
        state.takePictureDialogOpened = false
        with (context) {
            startActivityForResult(Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            },REQUEST_TAKE_PICTURE_FROM_PICTURE_LIBRARY)
        }
    }

    override fun onStateChanged(state: AppState, prevState: AppState) {
        val modelState = state.purchasesState
        val datePickerState = state.dateTimePickerState
        val prevDatePickerState = prevState.dateTimePickerState
        val prevSelectPlacesState = prevState.selectPlaceState
        val takePictureCameraState = state.takePictureCameraState
        val prevTakePictureCameraState = prevState.takePictureCameraState
        val mainState = state.mainState
        val prevMainState = prevState.mainState
        if (datePickerState.confirmed != prevDatePickerState.confirmed &&
            datePickerState.subscriberId == "Purchase_"+modelState.currentItemId) {
            modelState.dateTimePickerOpened = false
            if (datePickerState.dateSelected) {
                with (datePickerState) {
                    modelState.date = LocalDateTime.of(year,month+1,day,hour,minute)
                }
            }
        }
        if (state.mainState.orientation != prevState.mainState.orientation) updateForm()

        if (selectPlaceState.itemIsSelected != prevSelectPlacesState.itemIsSelected && selectPlaceState.itemIsSelected) {
            modelState.place = PlacesCollection.getItemById(selectPlaceState.currentItemId)
            modelState.selectPlaceDialogOpened = false
            updateForm()
        }
        if (takePictureCameraState.isConfirmed != prevTakePictureCameraState.isConfirmed) {
            if (takePictureCameraState.isPictureSubmitted) {
                onAddImageFromCamera(modelState)
                takePictureCameraState.isPictureSubmitted = false
            }
            updateForm()
        }
        if (mainState.orientation != prevMainState.orientation || mainState.lifecycleState != prevMainState.lifecycleState) {
            if (mainState.lifecycleState == LifecycleState.ON_PAUSE) {
                removeFragment(imagesPagerFragment)
                imagesPagerFragment = null
                removeFragment(dateTimePickerFragment)
                dateTimePickerFragment = null
                removeFragment(selectPlaceFragment)
                selectPlaceFragment = null
                removeFragment(takePictureCameraFragment)
                takePictureCameraFragment = null
            } else if (mainState.lifecycleState == LifecycleState.ON_RESUME) {
                updateForm()
            }

        }

        super.onStateChanged(state, prevState)
    }

    override fun onModelStateChanged(state: ModelState, prevState:ModelState) {
        val view = view as PurchaseComponent
        val state = state as PurchasesState
        val prevState = prevState as PurchasesState

        if (state.date != prevState.date || state.place != prevState.place) updateForm()
        if (state.dateTimePickerOpened != prevState.dateTimePickerOpened) { updateForm() }

        if (state.imageCapturedFromLibrary != prevState.imageCapturedFromLibrary && state.imageCapturedFromLibrary) {
            onAddImageFromLibrary(state)
        }
        if (state.takePictureDialogOpened != prevState.takePictureDialogOpened && state.takePictureDialogOpened) {
            updateForm()
        }
        if (state.imagesPagerOpened != prevState.imagesPagerOpened) { updateForm() }
        if (state.removeImageSelected != prevState.removeImageSelected && state.removeImageSelected) {
            state.images = state.images.apply { remove(state.currentImageId)}
            state.removeImageSelected = false
            state.imagesUpdateCounter += 1
        }
        if (state.imagesUpdateCounter != prevState.imagesUpdateCounter) view.imagesAdapter.notifyDataSetChanged()

        if (state.selectPlaceDialogOpened != prevState.selectPlaceDialogOpened) { updateForm() }

        if (state.takePictureCameraDialogOpened != prevState.takePictureCameraDialogOpened ) {
            updateForm()
        }


        super.onModelStateChanged(state, prevState)
    }

    override fun initNewItem(state:ModelState) {
        super.initNewItem(state)
        detectPlace(state as PurchasesState)
    }

    override fun initExistingItem(state:ModelState) {
        super.initExistingItem(state)
        syncImgCache(state as PurchasesState)
    }

    private fun onAddImageFromCamera(state:PurchasesState) {
        val file = File(CameraService.cameraFilePath)
        val destDir = "${PurchasesCollection.imgCachePath}/${state.currentItemId}"
        Files.createDirectories(Paths.get(destDir))
        val imageId = UUID.randomUUID().toString()
        state.images = state.images.apply { put(imageId, (file.lastModified()/1000).toString()) }
        if (Files.exists(Paths.get(file.absolutePath))) {
            Files.move(Paths.get(file.absolutePath), Paths.get("$destDir/$imageId.jpg"))
        } else {
            return
        }
        state.imagesUpdateCounter += 1
    }

    private fun onAddImageFromLibrary(state:PurchasesState) {
        val imageId = UUID.randomUUID().toString()
        val filePath = "${PurchasesCollection.imgCachePath}/${state.currentItemId}"
        Files.createDirectories(Paths.get(filePath))
        state.images = state.images.apply {put(imageId, (File("$filePath/$imageId.jpg").apply {
            outputStream().write(context.contentResolver.openInputStream(state.imageUri)!!.readBytes()
            )}.lastModified()/1000).toString())}
        state.imagesUpdateCounter += 1
    }

    private fun detectPlace(state:PurchasesState) {
        LocationManager.getLocation { lat, lng ->
            PlacesCollection.getClosestPlace(lat,lng) {
                state.place = it
                updateForm()
            }
        }
    }

    override fun onDataChange(items: ArrayList<Model>) {
        when (items.first()) {
            is Place -> updateForm()
            is Purchase -> {
                if (state.itemsUpdateCounter != 0) return
                syncImgCache(state as PurchasesState)
                state.itemsUpdateCounter +=1
            }
        }
    }

    private fun syncImgCache(state:PurchasesState) {
        if (state.item != null) {
            PurchasesCollection.syncImageCache(state.currentItemId, state.item!!.images, false) {
                (view as PurchaseComponent).imagesAdapter.notifyDataSetChanged()
            }
        }
    }
}

enum class ImageSource(val code:Int) { CAMERA(0), PICTURE_LIBRARY(1) }
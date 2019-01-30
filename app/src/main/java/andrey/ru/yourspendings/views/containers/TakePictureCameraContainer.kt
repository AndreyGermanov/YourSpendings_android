package andrey.ru.yourspendings.views.containers

import andrey.ru.yourspendings.services.CameraService
import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.components.TakePictureCameraComponent
import andrey.ru.yourspendings.views.store.AppState
import andrey.ru.yourspendings.views.store.LifecycleState
import andrey.ru.yourspendings.views.store.PurchasesState
import andrey.ru.yourspendings.views.store.TakePictureCameraState
import android.graphics.BitmapFactory
import android.view.View
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Andrey Germanov on 1/29/19.
 */
@Suppress("NAME_SHADOWING")
class TakePictureCameraContainer:Container() {

    lateinit var view: TakePictureCameraComponent
    lateinit var state: TakePictureCameraState
    private lateinit var purchaseState: PurchasesState

    override fun initialize(context: MainActivity) {
        super.initialize(context)
        view = TakePictureCameraComponent(context)
        component = view
        state = context.store.state.takePictureCameraState
        purchaseState = context.store.state.purchasesState
        initComponent()
        setListeners()
    }

    private fun initComponent() {
        view.render()
        setupUI()
    }

    private fun setupUI() {
        if (state.isPictureTaken) {
            with (view) {
                cameraView.visibility = View.GONE
                pictureView.visibility = View.VISIBLE
                exitBtn.visibility = View.GONE
                takePictureBtn.visibility = View.GONE
                previewBtn.visibility = View.GONE
                cancelBtn.visibility = View.VISIBLE
                confirmBtn.visibility = View.VISIBLE
                if (Files.exists(Paths.get(CameraService.cameraFilePath))) {
                    pictureView.setImageBitmap(
                        BitmapFactory.decodeFile(CameraService.cameraFilePath)
                    )
                }
            }
        } else {
            with (view) {
                cameraView.visibility = View.VISIBLE
                pictureView.visibility = View.GONE
                exitBtn.visibility = View.VISIBLE
                previewBtn.visibility = if (state.isPreviewEnabled) View.GONE else View.VISIBLE
                takePictureBtn.visibility = if (state.isPreviewEnabled) View.VISIBLE else View.GONE
                cancelBtn.visibility = View.GONE
                confirmBtn.visibility = View.GONE
            }
        }
    }

    fun setListeners() {
        with (view) {
            previewBtn.setOnClickListener {
                CameraService.setPreviewSurface(cameraView.holder)
                CameraService.openCamera{
                    CameraService.startPreview { state.isPreviewEnabled = true }
                }
            }
            takePictureBtn.setOnClickListener {
                CameraService.stopPreview()
                CameraService.takePicture {
                    state.isPreviewEnabled = false
                    state.isPictureTaken = true
                }
            }
            exitBtn.setOnClickListener {
                state.isPreviewEnabled = false
                state.isPictureSubmitted = false
                state.isConfirmed = true
                (context as MainActivity).store.state.purchasesState.takePictureCameraDialogOpened = false
            }
            confirmBtn.setOnClickListener {
                state.isPreviewEnabled = false
                state.isPictureSubmitted = true
                state.isConfirmed = true
                (context as MainActivity).store.state.purchasesState.takePictureCameraDialogOpened = false
            }
            cancelBtn.setOnClickListener {
                state.isPreviewEnabled = false
                state.isPictureTaken = false
            }
        }
    }

    override fun onStateChanged(state: AppState, prevState: AppState) {
        val localState = state.takePictureCameraState
        val localPrevState = prevState.takePictureCameraState

        if (localState.isPictureTaken != localPrevState.isPictureTaken) { setupUI() }

        if (localState.isPreviewEnabled != localPrevState.isPreviewEnabled) { setupUI() }

        if (localState.isConfirmed != localPrevState.isConfirmed) { CameraService.stopPreview() }

        if (state.purchasesState.takePictureCameraDialogOpened != prevState.purchasesState.takePictureCameraDialogOpened) {
            if (state.purchasesState.takePictureCameraDialogOpened)
                setupUI()
            else releaseCamera()
        }
        if (state.mainState.lifecycleState != prevState.mainState.lifecycleState) {
            if (state.purchasesState.takePictureCameraDialogOpened) {
                if (state.mainState.lifecycleState == LifecycleState.ON_PAUSE) {
                    localState.isPreviewEnabled = false
                    releaseCamera()
                }
                if (state.mainState.lifecycleState == LifecycleState.ON_RESUME) {
                    setupUI()
                }
            }
        }

        super.onStateChanged(state, prevState)
    }

    private fun releaseCamera() {
        CameraService.stopPreview()
        CameraService.closeCamera()

    }
}
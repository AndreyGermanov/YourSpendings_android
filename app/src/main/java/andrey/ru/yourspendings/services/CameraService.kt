@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@file:SuppressLint("ExifInterface")

package andrey.ru.yourspendings.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.CAMERA_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.hardware.camera2.*
import android.media.ExifInterface
import android.media.ImageReader
import android.view.Surface
import android.view.SurfaceHolder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by Andrey Germanov on 1/29/19.
 */
@SuppressLint("StaticFieldLeak")
object CameraService {
    private lateinit var manager: CameraManager
    lateinit var context: Activity
    private var camera: CameraDevice? = null
    private var session: CameraCaptureSession? = null
    private var previewSurfaceHolder: SurfaceHolder? = null
    private var isPreviewEnabled = false
    lateinit var cameraFilePath:String
    private var surfaceCallback: SurfaceHolder.Callback? = null

    fun initialize(context:Activity) {
        this.context = context
        manager = context.getSystemService(CAMERA_SERVICE) as CameraManager
        cameraFilePath = context.filesDir.absolutePath+"/images/camera_image.jpg"
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,arrayOf(Manifest.permission.CAMERA),1)
        }
    }

    @SuppressLint("MissingPermission")
    fun openCamera(callback:(error:Boolean)->Unit) {
        manager.openCamera("0", OnCameraOpen { cam,error ->
            camera = cam
            callback(error)
        },null)
    }

    fun closeCamera() {
        camera?.close()
    }

    fun setPreviewSurface(holder:SurfaceHolder) {
        if (previewSurfaceHolder == null) previewSurfaceHolder = holder
    }

    fun startPreview(callback:(error:Boolean)->Unit) {
        if (isPreviewEnabled) return
        openCaptureSession(previewSurfaceHolder!!.surface) { error ->
            if (!error) {
                startCaptureRequest(previewSurfaceHolder!!.surface, true) {
                    isPreviewEnabled = true
                    callback(error)
                }
            } else callback(error)
        }
    }

    fun stopPreview() {
        if (isPreviewEnabled) {
            session?.close()
            if (surfaceCallback!=null) previewSurfaceHolder!!.removeCallback(surfaceCallback)
        }
        isPreviewEnabled = false
    }

    fun takePicture(callback:()->Unit) {
        val maxSize = manager.getCameraCharacteristics("0")
            .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            .getOutputSizes(ImageFormat.JPEG)
            .maxBy { it.width * it.height}!!
        val imageReader = ImageReader.newInstance(maxSize.width,maxSize.height, ImageFormat.JPEG,1)
        imageReader.setOnImageAvailableListener(OnImageRead {
            session?.close()
            camera?.close()
            callback()
        },null)
        openCaptureSession(imageReader.surface) {
            startCaptureRequest(imageReader.surface,false) {
            }
        }

    }

    private fun openCaptureSession(surface:Surface,callback:(error:Boolean)->Unit) {
        camera?.createCaptureSession(listOf(surface), OnSessionOpen { sess, error ->
            session = sess
            callback(error)
        },null)
    }

    private fun startCaptureRequest(surface:Surface,repeating:Boolean,callback:()->Unit) {
        val cameraMode = if (repeating) CameraDevice.TEMPLATE_PREVIEW else CameraDevice.TEMPLATE_STILL_CAPTURE
        if (camera == null) return
        session?.abortCaptures()
        camera!!.createCaptureRequest(cameraMode).apply { addTarget(surface)
            set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation())
        }.build().also {
            if (repeating)
                session?.setRepeatingRequest(it,OnCapture{},null)
            else {
                session?.capture(it,OnCapture{},null)
            }
            callback()
        }
    }

    private fun getJpegOrientation(): Int {
        val deviceRotation = context.windowManager.defaultDisplay.rotation
        val characteristics = manager.getCameraCharacteristics("0")
        val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        val surfaceRotation = when(deviceRotation) {
            Surface.ROTATION_0 -> 90
            Surface.ROTATION_90 -> 0
            Surface.ROTATION_180 -> 270
            Surface.ROTATION_270 -> 180
            else -> 0
        }
        return (surfaceRotation + sensorOrientation + 270) % 360
    }

    class OnImageRead(val callback:()->Unit): ImageReader.OnImageAvailableListener {

        override fun onImageAvailable(reader: ImageReader?) {
            val img = reader!!.acquireLatestImage()
            val byteArray = ByteArray(img.planes[0].buffer.capacity())
            img.planes[0].buffer.get(byteArray)
            FileOutputStream(CameraService.cameraFilePath).apply { write(byteArray); flush(); close();}
            ExifInterface(FileInputStream(CameraService.cameraFilePath)).apply {
                getExifRotationAngle(getAttributeInt(ExifInterface.TAG_ORIENTATION,0)).also {
                    if (it>0) rotateImage(it)
                }
            }
            reader.close()
            callback()
        }

        private fun rotateImage(angle:Float) {
            val bmp = BitmapFactory.decodeFile(CameraService.cameraFilePath)
            val mtx = Matrix()
            mtx.postRotate(angle)
            val bmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, mtx, true)
            val fout = FileOutputStream(CameraService.cameraFilePath)
            bmp2.compress(Bitmap.CompressFormat.JPEG, 85, fout)
            fout.apply { flush();close() }
            bmp.recycle()
            bmp2.recycle()
        }

        private fun getExifRotationAngle(attributeCode:Int):Float = when(attributeCode) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90.0f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180.0f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270.0f
            else -> 0.0f
        }

    }

    class OnCameraOpen(val callback:(device:CameraDevice,error:Boolean)->Unit): CameraDevice.StateCallback() {
        override fun onOpened(p0: CameraDevice) { callback(p0,false) }
        override fun onDisconnected(p0: CameraDevice) { callback(p0,true) }
        override fun onError(p0: CameraDevice, p1: Int) { callback(p0,true) }
    }

    class OnSessionOpen(val callback:(session: CameraCaptureSession,error:Boolean)->Unit): CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(p0: CameraCaptureSession) { callback(p0,true) }
        override fun onConfigured(p0: CameraCaptureSession) { callback(p0,false) }
    }

    class OnCapture(val callback:(result: TotalCaptureResult)->Unit): CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            super.onCaptureCompleted(session, request, result)
            callback(result)
        }
    }
}
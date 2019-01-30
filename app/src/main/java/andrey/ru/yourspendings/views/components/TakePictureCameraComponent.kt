package andrey.ru.yourspendings.views.components

import andrey.ru.yourspendings.R
import andrey.ru.yourspendings.views.MainActivity
import android.annotation.SuppressLint
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout

/**
 * Created by Andrey Germanov on 1/29/19.
 */
@SuppressLint("ViewConstructor")
class TakePictureCameraComponent(context: MainActivity):Component(context) {

    lateinit var cameraView: SurfaceView
    lateinit var pictureView: ImageView
    lateinit var previewBtn: Button
    lateinit var takePictureBtn: Button
    lateinit var exitBtn: Button
    lateinit var confirmBtn: Button
    lateinit var cancelBtn: Button

    override fun render() {
        LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL
            cameraView = SurfaceView(context).apply {
                layoutParams = fullScreen().apply { weight = 10.0f }
            }.also { this.addView(it) }
            pictureView = ImageView(context).apply {
                layoutParams = fullScreen().apply { weight = 10.0f }
            }.also { this.addView(it) }
            previewBtn = button(context.getString(R.string.preview)).also { this.addView(it)}
            takePictureBtn = button(context.getString(R.string.take_a_picture)).also { this.addView(it) }
            exitBtn = button(context.getString(R.string.exit)).also { this.addView(it) }
            confirmBtn = button(context.getString(R.string.confirm)).also { this.addView(it) }
            cancelBtn = button(context.getString(R.string.cancel)).also { this.addView(it) }
        }.also { this.addView(it) }
    }
}
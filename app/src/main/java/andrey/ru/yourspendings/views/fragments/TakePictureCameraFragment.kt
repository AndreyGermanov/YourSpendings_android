package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.containers.Container
import andrey.ru.yourspendings.views.containers.TakePictureCameraContainer
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Andrey Germanov on 1/29/19.
 */
class TakePictureCameraFragment: FullScreenFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return Container.getInstance(activity as MainActivity,TakePictureCameraContainer::class.java).setView()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        with ((activity as MainActivity).store.state.takePictureCameraState) {
            isPictureSubmitted = false
            isConfirmed = true
        }
        (activity as MainActivity).store.state.purchasesState.takePictureCameraDialogOpened = false
    }
}
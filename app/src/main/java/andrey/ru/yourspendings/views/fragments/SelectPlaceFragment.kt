package andrey.ru.yourspendings.views.fragments

import andrey.ru.yourspendings.views.MainActivity
import andrey.ru.yourspendings.views.containers.Container
import andrey.ru.yourspendings.views.containers.PlacesScreenContainer
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Andrey Germanov on 1/24/19.
 */
class SelectPlaceFragment: FullScreenFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val activity = activity as MainActivity
        return Container.getModelInstance(activity,PlacesScreenContainer::class.java,
            activity.store.state.selectPlaceState).setView()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        val activity = activity as MainActivity
        activity.store.state.purchasesState.selectPlaceDialogOpened = false
    }

}
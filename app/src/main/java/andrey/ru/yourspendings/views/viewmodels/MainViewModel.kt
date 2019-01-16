package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.PlacesCollection
import andrey.ru.yourspendings.models.PurchasesCollection
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.services.IAuthServiceSubscriber
import androidx.lifecycle.MutableLiveData

/**
 * Created by Andrey Germanov on 1/8/19.
 */
object MainViewModel: PersistedViewModel(),IAuthServiceSubscriber {

    private var mScreen: Screens = Screens.LOGIN
    val screenObserver:MutableLiveData<Screens> = MutableLiveData()

    var screen:Screens
        get() = mScreen
        set(value) {
            mScreen = value
            screenObserver.postValue(value)
            save()
        }

    private val subscribers: ArrayList<ActivityEventSubscriber> = ArrayList()

    init { AuthManager.subscribe(this); PlacesCollection.loadList() }


    fun logout() {
        PurchasesCollection.clear()
        PlacesCollection.clear()
        AuthManager.logout()
    }

    override fun onAuthStatusChanged(isAuthenticated: Boolean) {
        if (!isAuthenticated) screen = Screens.LOGIN else {
            val state = getState()
            var stateScreen = state["screen"]?.toString() ?: "DASHBOARD"
            if (stateScreen == "LOGIN" || stateScreen == "REGISTER") stateScreen = "DASHBOARD"
            screen = Screens.valueOf(stateScreen)
        }
    }

    fun subscribe(subscriber:ActivityEventSubscriber) {
        if (!subscribers.contains(subscriber)) subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber:ActivityEventSubscriber) {
        if (subscribers.contains(subscriber)) subscribers.remove(subscriber)
    }

    fun triggerEvent(event:ActivityEvent) = subscribers.forEach { it.onActivityEvent(event)}

    override fun getState():HashMap<String,Any> = hashMapOf("screen" to mScreen)

    override fun setState(state:HashMap<String,Any>) {
        screen = Screens.valueOf(state["screen"]?.toString() ?: "LOGIN")
    }

}

interface ActivityEventSubscriber {
    fun onActivityEvent(event:ActivityEvent)
}

data class ActivityEvent(val subscriberId:String,val eventName:String,val eventData:Any?)

enum class Screens {LOGIN,DASHBOARD,PLACES,PURCHASES,NEW_PURCHASE}


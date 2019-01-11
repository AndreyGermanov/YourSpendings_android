package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.models.PlacesCollection
import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.services.IAuthServiceSubscriber
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Andrey Germanov on 1/8/19.
 */
class MainViewModel: ViewModel(),IAuthServiceSubscriber {

    private val screen: MutableLiveData<Screens> = MutableLiveData()
    private val subscribers: ArrayList<ActivityEventSubscriber> = ArrayList()

    init { AuthManager.subscribe(this); PlacesCollection.loadList() }

    fun getScreen() = screen
    fun getCurrentScreen() = screen.value ?: Screens.LOGIN
    fun setScreen(screen:Screens) = this.screen.postValue(screen)

    fun logout() { AuthManager.logout() }

    override fun onAuthStatusChanged(isAuthenticated: Boolean) {
        if (!isAuthenticated) screen.postValue(Screens.LOGIN) else screen.postValue(Screens.DASHBOARD)
    }

    fun subscribe(subscriber:ActivityEventSubscriber) {
        if (!subscribers.contains(subscriber)) subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber:ActivityEventSubscriber) {
        if (subscribers.contains(subscriber)) subscribers.remove(subscriber)
    }

    fun triggerEvent(event:ActivityEvent) = subscribers.forEach { it.onActivityEvent(event)}

}

interface ActivityEventSubscriber {
    fun onActivityEvent(event:ActivityEvent)
}

data class ActivityEvent(val subscriberId:String,val eventName:String,val eventData:Any?)

enum class Screens {LOGIN,DASHBOARD,PLACES,PURCHASES}


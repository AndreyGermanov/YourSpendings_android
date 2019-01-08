package andrey.ru.yourspendings.views.viewmodels

import andrey.ru.yourspendings.services.AuthManager
import andrey.ru.yourspendings.services.IAuthServiceSubscriber
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Andrey Germanov on 1/8/19.
 */
class MainViewModel: ViewModel(),IAuthServiceSubscriber {

    private val screen: MutableLiveData<Screens> = MutableLiveData()

    init { AuthManager.subscribe(this) }

    fun getScreen() = screen
    fun getCurrentScreen() = screen.value ?: Screens.LOGIN
    fun setScreen(screen:Screens) = this.screen.postValue(screen)

    fun logout() { AuthManager.logout() }

    override fun onAuthStatusChanged(isAuthenticated: Boolean) {
        if (!isAuthenticated) screen.postValue(Screens.LOGIN) else screen.postValue(Screens.DASHBOARD)
    }

}

enum class Screens {LOGIN,DASHBOARD,PLACES,PURCHASES}


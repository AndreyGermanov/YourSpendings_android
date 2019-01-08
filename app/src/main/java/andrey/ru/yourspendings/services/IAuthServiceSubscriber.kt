package andrey.ru.yourspendings.services

/**
 * Created by Andrey Germanov on 1/8/19.
 */
interface IAuthServiceSubscriber {

    fun onAuthStatusChanged(isAuthenticated:Boolean)

}
package andrey.ru.yourspendings.views.store

/**
 * Created by Andrey Germanov on 1/18/19.
 */
interface IStoreSubscriber {
    fun onStateChanged(state:AppState,prevState:AppState)
}
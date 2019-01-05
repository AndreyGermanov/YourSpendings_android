package andrey.ru.yourspendings.models

/**
 * Created by Andrey Germanov on 1/5/19.
 */
interface IDataCollection {
    fun subscribe(subscriber:IDataSubscriber)
    fun unsubscribe(subscriber: IDataSubscriber)
}
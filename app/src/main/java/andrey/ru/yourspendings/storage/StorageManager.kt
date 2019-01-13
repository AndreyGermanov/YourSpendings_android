package andrey.ru.yourspendings.storage

/**
 * Created by Andrey Germanov on 1/12/19.
 */
object StorageManager {
    fun getStorage():IStorageAdapter = FireStorageAdapter()
}
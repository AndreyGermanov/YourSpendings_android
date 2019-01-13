package andrey.ru.yourspendings.storage

/**
 * Created by Andrey Germanov on 1/12/19.
 */
interface IStorageAdapter {
    fun putFile(sourcePath: String, destinationPath: String, callback: (error: String?) -> Unit)
    fun deleteFile(path: String, callback: (error: String?) -> Unit)
    fun getFile(sourcePath:String, destinationPath: String, callback:(error:String?)->Unit)
}
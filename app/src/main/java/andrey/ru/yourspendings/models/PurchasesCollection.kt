package andrey.ru.yourspendings.models

import andrey.ru.yourspendings.storage.StorageManager
import android.annotation.SuppressLint
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Created by Andrey Germanov on 1/9/19.
 */
@SuppressLint("StaticFieldLeak")
object PurchasesCollection: Collection<Purchase>() {

    override val tableName
        get() = "purchases"

    override fun getCollectionName(): String = "purchases"

    override fun getListTitle() = "Purchases List"

    override fun newItem(data:Map<String,Any>):Purchase = Purchase.fromHashMap(data)

    override fun newItemFromDB(data:Map<String,Any>):Purchase = Purchase.fromHashMapOfDB(data)

    override fun validateItem(fields: HashMap<String, Any>, callback: (result: Any) -> Unit) {
        if ((fields["place_id"]?.toString() ?: "").isEmpty()) {callback("Place must be specified ");return;}
        if ((fields["date"]?.toString() ?: "").isEmpty()) {callback("Date must be specified ");return;}
        val item = newItem(fields)
        if (item.id == "new") {
            val basePath = "${ctx.filesDir.absolutePath}/images"
            item.id = UUID.randomUUID().toString()
            if (Files.exists(Paths.get("$basePath/new")))
                Files.move(Paths.get("$basePath/new"),Paths.get("$basePath/${item.id}"))
        }
        syncImages(getItemById(item.id),item) { error -> if (error) callback(error); else callback(item) }
    }

    override fun deleteItem(id: String, callback: (error: String?) -> Unit) {
        val oldItem = getItemById(id)
        if (oldItem != null) {
            val newItem = newItem(oldItem.toHashMap())
            (newItem.images as HashMap<String,String>).clear()
            syncImages(oldItem,newItem) {
                super.deleteItem(id, callback)
            }
        } else super.deleteItem(id, callback)
    }

    private fun syncImages(oldItem:Purchase?,newItem:Purchase,callback:(result:Boolean)->Unit) {
        val storage = StorageManager.getStorage()
        syncImageCache(newItem.id,newItem.images) {
            val oldImages = oldItem?.images ?: HashMap()
            val imagesToAdd = newItem.images.filter {
                !oldImages.containsKey(it.key) || it.value.toLong() > oldImages[it.key]!!.toLong()
            }
            val imagesToRemove = oldImages.filter { !newItem.images.containsKey(it.key) }
            uploadImages(newItem.id,imagesToAdd) { uploadError ->
                if (!uploadError) removeImages(newItem.id, imagesToRemove) { removeError ->
                    if (newItem.images.isEmpty())
                        storage.deleteFile("images/${newItem.id}") { callback(it?.isEmpty() ?: true); }
                    else callback(removeError)
                } else callback(uploadError)
            }
        }
    }

    fun syncImageCache(itemId:String,images:Map<String,String>,callback:()->Unit) {
        val path = Paths.get("${ctx.filesDir.absolutePath}/images/$itemId/")
        if (!Files.exists(path)) Files.createDirectories(path)
        if (path.toFile().list().isNotEmpty()) {
            Files.walk(path).forEach {
                if (!images.containsKey(it.toFile().nameWithoutExtension)) it.toFile().delete()
            }
            try {
                Files.walk(Paths.get("${ctx.filesDir.absolutePath}/images/")).forEach {
                    if (it.toFile().isDirectory && it.toFile().list().isEmpty()) it.toFile().delete()
                }
            } catch (e:Exception) { callback() }
        }

        var counter = 0
        val storage = StorageManager.getStorage()

        if (images.keys.isEmpty()) {callback();return;}
        images.keys.forEach {
            if (!Files.exists(path)) Files.createDirectories(path)
            if (Files.notExists(Paths.get("${ctx.filesDir.absolutePath}/images/$itemId/$it.jpg"))) {
                storage.getFile("images/$itemId/$it.jpg","${ctx.filesDir.absolutePath}/images/$itemId/$it.jpg") {
                    counter += 1
                    if (counter == images.size) callback()
                }
            } else {
                counter += 1
                if (counter == images.size) callback()
            }
        }
    }

    private fun uploadImages(itemId:String,images:Map<String,String>,callback:(result:Boolean)->Unit) {
        if (images.isEmpty()) { callback(false); return; }
        val storage = StorageManager.getStorage()
        var counter = 0
        var error = false
        images.keys.forEach {imageId ->
            if (error) return
            val path = "${ctx.filesDir.absolutePath}/images/$itemId/$imageId.jpg"
            storage.putFile(path, "images/$itemId/$imageId.jpg") {
                if (it==null) counter++; else { error=true;callback(error) }
                if (counter == images.size) {callback(false);}
            }
        }
    }

    private fun removeImages(itemId:String,images:Map<String,String>,callback:(result:Boolean)->Unit) {
        if (images.isEmpty()) { callback(false); return; }
        val storage = StorageManager.getStorage()
        var counter = 0
        var error = false
        images.keys.forEach { imageId ->
            if (error) return
            storage.deleteFile("images/$itemId/$imageId.jpg") {
                if (it==null) counter++; else { error = true;callback(error) }
                if (counter == images.size) callback(false)
            }
        }
    }

}
package andrey.ru.yourspendings.storage

import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Andrey Germanov on 1/12/19.
 */
class FireStorageAdapter(private val storage:FirebaseStorage = FirebaseStorage.getInstance()) : IStorageAdapter {

    override fun getFile(sourcePath:String, destinationPath: String, callback:(error:String?)->Unit) {
        storage.reference.child(sourcePath).getFile(File(destinationPath))
            .addOnSuccessListener { callback(null) }
            .addOnFailureListener { callback(it.message) }
    }

    override fun putFile(sourcePath:String, destinationPath:String, callback:(error:String?)->Unit) {
        if (!Files.exists(Paths.get(sourcePath))) { callback("File not found - $sourcePath");return; }
        storage.reference.child(destinationPath).putStream(FileInputStream(sourcePath))
            .addOnSuccessListener { callback(null) }
            .addOnFailureListener { callback(it.message) }
    }

    override fun deleteFile(path:String, callback:(error:String?)->Unit) {
        storage.reference.child(path).delete()
            .addOnSuccessListener { callback(null) }
            .addOnFailureListener { callback(it.message) }
    }
}
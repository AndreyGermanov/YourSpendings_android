package andrey.ru.yourspendings.db

import andrey.ru.yourspendings.models.IDatabaseSubscriber
import andrey.ru.yourspendings.services.AuthManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*

/**
 * Created by Andrey Germanov on 1/4/19.
 */
class FirebaseAdapter
    (
        private val db:FirebaseFirestore = FirebaseFirestore.getInstance(),
        private val subscribers:HashMap<String,ListenerRegistration> = HashMap()
    ): IDatabaseAdapter {

    override fun getList(collectionName: String,callback:(List<Map<String,Any>>) -> Unit ) {
        db.collection(collectionName).whereEqualTo("user_id",AuthManager.user?.uid).get().addOnSuccessListener { snapshot ->
            if (snapshot.documents.isNotEmpty()) {
                val items: List<Map<String, Any>> = snapshot.documents.map { document ->
                    val item = document.data!!
                    item["id"] = document.id
                    item
                }
                callback(items)
            } else {
                callback(ArrayList())
            }
        }.addOnFailureListener {
            callback(ArrayList())
        }
    }

    override fun saveItem(collectionName:String,data:HashMap<String,Any>,callback:(result:String?)->Unit) {
        val id = data["id"]?.toString() ?: return
        data["user_id"] = AuthManager.user?.uid!!
        data.remove(id)
        db.collection(collectionName).document(id)
            .set(data).addOnSuccessListener { callback(null) }
            .addOnFailureListener { callback(it.message) }
    }

    override fun deleteItem(collectionName:String,id:String,callback:(error:String?)->Unit) {
        db.collection(collectionName).document(id).delete()
            .addOnSuccessListener { callback(null) }
            .addOnFailureListener { callback(it.message)}
    }

    override fun subscribe(subscriber: IDatabaseSubscriber) {
        val collectionName = subscriber.getCollectionName()
        if (!subscribers.containsKey(collectionName)) {
            subscribers[collectionName] = db.collection(collectionName)
                .whereEqualTo("user_id",AuthManager.user?.uid).addSnapshotListener { snapshots, _ ->
                    subscriber.onDataChange(snapshots!!.documentChanges.asSequence().map { change ->
                        val item = change.document.data
                        item["changeType"] = change.type.toString()
                        item["id"] = change.document.id
                        item
                    }.groupBy{ item -> item["changeType"]!!.toString()})
            }
        }
    }

    override fun unsubscribe(subscriber: IDatabaseSubscriber) {
        val collectionName = subscriber.getCollectionName()
        if (subscribers.containsKey(collectionName)) {
            subscribers[collectionName]!!.remove()
            subscribers.remove(collectionName)
        }
    }

}
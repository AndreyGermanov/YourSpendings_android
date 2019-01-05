package andrey.ru.yourspendings.db

import andrey.ru.yourspendings.models.IDatabaseSubscriber
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

/**
 * Created by Andrey Germanov on 1/4/19.
 */
class FirebaseAdapter
    (
        private val db:FirebaseFirestore = FirebaseFirestore.getInstance(),
        private val subscribers:HashMap<String,ListenerRegistration> = HashMap()
    ): IDatabaseAdapter {

    override fun getList(collectionName: String,callback:(List<Map<String,Any>>) -> Unit ) {
        db.collection(collectionName).get().addOnSuccessListener {
            if (it.documents.isNotEmpty()) {
                val items: List<Map<String, Any>> = it.documents.map {
                    val item = it.data!!
                    item["id"] = it.id
                    item
                }
                callback(items)
            }
        }
    }

    override fun subscribe(subscriber: IDatabaseSubscriber) {
        val collectionName = subscriber.getCollectionName()
        if (!subscribers.containsKey(collectionName)) {
            subscribers[collectionName] = db.collection(collectionName).addSnapshotListener { snapshots, _ ->
                subscriber.onDataChange(snapshots!!.documentChanges.asSequence().map {
                    val item = it.document.data
                    item["changeType"] = it.type.toString()
                    item["id"] = it.document.id
                    item
                }.groupBy{ it -> it["changeType"]!!.toString()})
            }
        }
    }

    override fun unsubscribe(subscriber: IDatabaseSubscriber) {
        val collectionName = subscriber.getCollectionName()
        if (subscribers.containsKey(collectionName)) subscribers[collectionName]!!.remove()
    }
}
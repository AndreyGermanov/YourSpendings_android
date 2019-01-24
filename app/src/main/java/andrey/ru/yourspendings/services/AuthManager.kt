package andrey.ru.yourspendings.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Andrey Germanov on 1/7/19.
 */
object AuthManager {
    private val auth = FirebaseAuth.getInstance()
    private val subscribers = ArrayList<IAuthServiceSubscriber>()

    val user: FirebaseUser?
        get() = auth.currentUser

    val userId: String
        get() = auth.currentUser?.uid ?: ""

    init {
        auth.addAuthStateListener { onAuthStateChanged(it); }
    }

    fun register(email:String,password:String,callback:(error:String?)->Unit) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener { callback(null) }
            .addOnFailureListener { callback(it.message) }
    }

    fun login(email:String,password:String,callback:(error:String?)->Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener { callback(null) }
            .addOnFailureListener { callback(it.message) }
    }

    fun logout() = auth.signOut()

    fun subscribe(subscriber:IAuthServiceSubscriber) {
        if (!subscribers.contains(subscriber)) subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber: IAuthServiceSubscriber) {
        if (subscribers.contains(subscriber)) subscribers.remove(subscriber)
    }

    private fun onAuthStateChanged(auth:FirebaseAuth) {
        subscribers.forEach { it.onAuthStatusChanged(auth.currentUser != null) }
    }

}
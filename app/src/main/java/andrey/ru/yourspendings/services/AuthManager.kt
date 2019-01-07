package andrey.ru.yourspendings.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Andrey Germanov on 1/7/19.
 */
object AuthManager {
    private val auth = FirebaseAuth.getInstance()

    init {
       auth.signOut()
    }

    val user: FirebaseUser?
        get() = auth.currentUser

    val isLogin:Boolean
        get() = user != null

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

}
package com.example.notepad.model


import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class ConnectToFirebase(private val application: Application){

    private var onlineUserId = ""

    val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser?>
    private var auth: FirebaseAuth? = null
    var handler:Handler?=null


    suspend fun register(email: String,pass:String): Boolean {
        try {
            auth?.createUserWithEmailAndPassword(email,pass)?.await()
            firebaseUserMutableLiveData.postValue(auth!!.currentUser)
            return true
        } catch(e: Exception) {
            handler=Handler(Looper.getMainLooper())
            handler!!.post {
                Toast.makeText(application, e.message, Toast.LENGTH_LONG).show()
            }
            return false
        }
    }

    suspend fun login(email: String, pass: String):Boolean {

        try {
            auth?.signInWithEmailAndPassword(email, pass)?.await()
            firebaseUserMutableLiveData.postValue(auth!!.currentUser)
            return true
        } catch (e: Exception) {
            handler=Handler(Looper.getMainLooper())
            handler!!.post {
                Toast.makeText(application, e.message, Toast.LENGTH_LONG).show()
            }
            return false
        }
    }


    fun addnote(title: String?, description: String?) {

        auth = FirebaseAuth.getInstance()
        onlineUserId = auth!!.currentUser?.uid.toString()
        val database = Firebase.database
        val myRef =database.getReference("USERS").child(onlineUserId)
        val id: String? = myRef.push().key
        val Data = Data(title.toString(), description.toString())
        if (id != null) {
            myRef.child(id).setValue(Data)
        }

    }

    suspend fun getUserData(): ArrayList<Data>? {

        lateinit var response: ArrayList<Data>
        try {
            onlineUserId = auth!!.currentUser?.uid.toString()
            Log.d(onlineUserId,"current user signed in")
            val database = Firebase.database
            val productRef=database.reference.child("USERS").child(onlineUserId)
            response= productRef.get().await().children.map { snapShot:DataSnapshot ->
                snapShot.getValue(Data::class.java)!!
            } as ArrayList<Data>
            return response
        } catch (exception: Exception) {
            handler=Handler(Looper.getMainLooper())
            handler!!.post {
                Toast.makeText(application, exception.message, Toast.LENGTH_SHORT).show()
            }
            return null
        }
    }

    fun delete() {
        val reference = FirebaseDatabase.getInstance().reference.child("USERS").child(onlineUserId)
        reference.removeValue()
    }

    fun update(title: String, desc: String) {
        val data =Data(title,desc)
        val reference = FirebaseDatabase.getInstance().reference.child("USERS").child(onlineUserId)
        reference.removeValue()
        reference.push().setValue(data)
    }

    fun passReset(email: String) {
        auth= FirebaseAuth.getInstance()
        auth!!.sendPasswordResetEmail(email).addOnCompleteListener { task->
            if(task.isSuccessful)
                Toast.makeText(application,"Email is sent to reset your password",Toast.LENGTH_LONG).show()
            else
                Toast.makeText(application, task.exception?.message,Toast.LENGTH_LONG).show()
        }
    }


    init {
        firebaseUserMutableLiveData = MutableLiveData()
        auth = FirebaseAuth.getInstance()
        if (auth!!.currentUser != null) {
            firebaseUserMutableLiveData.postValue(auth!!.currentUser)
        }
    }
}


package com.example.notepad.viewmodel
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.notepad.model.ConnectToFirebase
import com.example.notepad.model.Data

class Logic(application: Application) : AndroidViewModel(application) {
    private val CTF: ConnectToFirebase

    suspend fun register(email: String, pass: String): Boolean {
        return CTF.register(email, pass)
    }

    suspend fun login(email: String, pass: String): Boolean {
        return CTF.login(email, pass)
    }


    fun addnote(title: String?, description: String?) {
        CTF.addnote(title, description)
    }

    suspend fun getUserData(): ArrayList<Data> {
        val responseLiveData:ArrayList<Data>
        responseLiveData= CTF.getUserData()!!
        Log.d(responseLiveData.toString(),"Live Data")
        return responseLiveData
    }

    fun update(title: String, desc: String) {
        CTF.update(title,desc)

    }

    fun delete() {
        CTF.delete()
    }

    fun passReset(email: String) {
        CTF.passReset(email)
    }
    init {
            CTF = ConnectToFirebase(application)
        }
    }

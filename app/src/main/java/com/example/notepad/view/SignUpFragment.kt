package com.example.notepad.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notepad.R
import com.example.notepad.model.ConnectToFirebase
import com.example.notepad.viewmodel.Logic
import kotlinx.coroutines.*

class SignUpFragment : Fragment() {

    private var emailEdit: EditText? = null
    private var passEdit: EditText? = null
    private var passEdit1: EditText? = null
    private var signInText: TextView? = null
    private var signUpBtn: Button? = null
    private var viewmodel: Logic? = null
    private var navController: NavController? = null
    private var scope: CoroutineScope = MainScope()
    private lateinit var progressBar: ProgressBar


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        )[Logic::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEdit = view.findViewById(R.id.email)
        passEdit = view.findViewById(R.id.password)
        passEdit1 = view.findViewById(R.id.password1)
        signInText = view.findViewById(R.id.login_btn)
        signUpBtn = view.findViewById(R.id.signup_btn)
        progressBar = view.findViewById(R.id.progressBar2)
        navController = Navigation.findNavController(view)
        var flag1: Boolean

        progressBar.visibility = View.INVISIBLE
        signInText?.setOnClickListener {
            navController!!.navigate(R.id.action_signUpFragment2_to_signInFragment2)
        }

        signUpBtn?.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = emailEdit?.text.toString()
            val pass = passEdit?.text.toString()
            val pass1 = passEdit1?.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty() && pass1.isNotEmpty()) {
                if (pass == pass1) {
                    scope.launch {
                        flag1 = withContext(scope.coroutineContext + Dispatchers.IO) {
                            viewmodel?.register(email, pass) == true
                        }
                        if (flag1) {
                            navController?.navigate(R.id.action_signUpFragment2_to_clickAddNewNoteFragment)
                        }
                        progressBar.visibility = View.INVISIBLE
                    }
                } else
                    Toast.makeText(context, "Entered password is wrong", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(context, "Fill all the details", Toast.LENGTH_LONG).show()
        }

    }

}
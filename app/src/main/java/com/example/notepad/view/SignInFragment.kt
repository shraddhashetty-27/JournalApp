package com.example.notepad.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notepad.R
import com.example.notepad.model.ConnectToFirebase
import com.example.notepad.viewmodel.Logic
import kotlinx.coroutines.*

class SignInFragment : Fragment() {
    private var email: EditText? = null
    private var pass: EditText? = null
    private var signIn: Button? = null
    private var signUp: Button? = null
    private var passReset: TextView? = null
    private var scope: CoroutineScope = MainScope()
    private var viewmodel: Logic? = null
    private var navController: NavController?=null
    private lateinit var progressBar:ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[Logic::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = view.findViewById(R.id.email_edt_text)
        pass = view.findViewById(R.id.pass_edt_text)
        signIn = view.findViewById(R.id.login_btn)
        passReset=view.findViewById(R.id.password_reset)
        signUp = view.findViewById(R.id.signup_btn)
        progressBar=view.findViewById(R.id.progressBar)
        navController = Navigation.findNavController(view)
        var flag1: Boolean

        progressBar.visibility=View.INVISIBLE
        signIn?.setOnClickListener(View.OnClickListener{
            progressBar.visibility=View.VISIBLE
            val email = email?.text.toString()
            val pass = pass?.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                scope.launch {
                    flag1 = withContext(scope.coroutineContext+Dispatchers.IO) {viewmodel?.login(email, pass) == true}
                    if (flag1) {
                        val data = withContext(scope.coroutineContext + Dispatchers.IO) { viewmodel?.getUserData()!! }
                        if (data.isEmpty()) {
                            navController?.navigate(R.id.action_signInFragment2_to_clickAddNewNoteFragment2)
                        } else {
                            navController?.navigate(R.id.action_signInFragment2_to_noteFragment)
                        }
                    }
                    progressBar.visibility = View.INVISIBLE
                }
            }
        })
        signUp?.setOnClickListener(View.OnClickListener {
            navController!!.navigate(R.id.action_signInFragment2_to_signUpFragment2)
            //ConnectToFirebase.shared
        })
        passReset?.setOnClickListener(View.OnClickListener {
            val email = email?.text.toString()
            if (email=="")
                Toast.makeText(context,"Please enter your Email",Toast.LENGTH_LONG).show()
            else {
                viewmodel?.passReset(email)
            }
        })

    }

}
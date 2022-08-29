package com.example.notepad.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
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

//import com.example.notepad.viewmodel.Logic


class AddNewNoteFragment : Fragment() {

    private var title: EditText? = null
    private var description: EditText? = null
    private var save: Button? = null
    private var viewmodel: Logic? = null
    private var navController: NavController? = null

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
        return inflater.inflate(R.layout.fragment_add_new_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title=view.findViewById(R.id.title)
        description=view.findViewById(R.id.description)
        title?.movementMethod = ScrollingMovementMethod()
        description?.movementMethod=ScrollingMovementMethod()
        save=view.findViewById(R.id.savebutton)
        navController = Navigation.findNavController(view)
        save?.setOnClickListener(View.OnClickListener{
            val title = title?.getText().toString()
            val description = description?.getText().toString()
            if (title.isNotEmpty() && description.isNotEmpty()) {
                viewmodel?.addnote(title, description)
                navController?.navigate(R.id.action_addNewNoteFragment_to_noteFragment)
            }
            else{
                Toast.makeText(context,"Please fill the required fields",Toast.LENGTH_SHORT).show()
            }
        })
    }

}
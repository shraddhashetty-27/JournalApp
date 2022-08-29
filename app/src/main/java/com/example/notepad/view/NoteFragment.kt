package com.example.notepad.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notepad.R
import com.example.notepad.model.Data
import com.example.notepad.viewmodel.Logic
import kotlinx.coroutines.*

class NoteFragment : Fragment() {
    private var scope: CoroutineScope = MainScope()
    lateinit var signout:Button
    var title: EditText? =null
    lateinit var update:Button
    lateinit var delete:Button
    lateinit var description:EditText
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
        return inflater.inflate(R.layout.fragment_note, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update=view.findViewById(R.id.update)
        delete=view.findViewById(R.id.delete)
        signout = view.findViewById(R.id.signout)
        title=view.findViewById(R.id.title1)
        description=view.findViewById(R.id.description1)

        navController = Navigation.findNavController(view)

        var data:ArrayList<Data>?=null
        scope.launch {
            data = withContext(scope.coroutineContext+Dispatchers.IO){viewmodel?.getUserData()!!}
            Log.d(data.toString(),"data")
            title?.setText("${data?.get(0)?.title}")
            description.setText("${data?.get(0)?.description}")
        }

        update.setOnClickListener {
            val title = title?.text.toString()
            val desc = description.text.toString()
            viewmodel?.update(title, desc)
            Toast.makeText(context, "Note updated successfully", Toast.LENGTH_LONG).show()
        }

        delete.setOnClickListener {
            viewmodel?.delete()
            Toast.makeText(context, "Note Deleted Successfully", Toast.LENGTH_LONG).show()
            navController?.navigate(R.id.action_noteFragment_to_clickAddNewNoteFragment)
        }

        signout.setOnClickListener {
            navController?.navigate(R.id.action_noteFragment_to_signInFragment2)
        }
    }
}
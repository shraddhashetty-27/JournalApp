package com.example.notepad.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notepad.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClickAddNewNoteFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var plus:FloatingActionButton
    private var navController: NavController? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_click_add_new_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        plus=view.findViewById(R.id.plusicon)
        plus.setOnClickListener(View.OnClickListener {
            navController!!.navigate(R.id.action_clickAddNewNoteFragment_to_addNewNoteFragment)
        })
    }

}
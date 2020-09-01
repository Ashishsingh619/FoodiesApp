package com.example.assignment2.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.example.assignment2.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    lateinit var sharedpreference: SharedPreferences
    lateinit var et_profileName: TextView
    lateinit var et_profilePhone: TextView
    lateinit var et_profileEmail: TextView
    lateinit var et_profileAdress: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        sharedpreference = activity!!.getSharedPreferences(
            getString(R.string.shared_file_name),
            Context.MODE_PRIVATE
        )
        et_profileName = view.findViewById(R.id.et_profileName)
        et_profilePhone = view.findViewById(R.id.et_profilePhone)
        et_profileEmail = view.findViewById(R.id.et_profileEmail)
        et_profileAdress = view.findViewById(R.id.et_profileAdress)
        et_profileName.text = sharedpreference.getString("Pname", "Name")
        et_profilePhone.text = sharedpreference.getString("Pphone", "Number")
        et_profileEmail.text = sharedpreference.getString("Pemail", "Email")
        et_profileAdress.text = sharedpreference.getString("Paddress", "Address")

        return view
    }


}

package com.maouni92.runtracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.maouni92.runtracker.R
import com.maouni92.runtracker.databinding.FragmentSetupBinding
import com.maouni92.runtracker.databinding.FragmentTrackingBinding
import com.maouni92.runtracker.helper.Constants.KEY_FIRST_TIME_TOGGLE
import com.maouni92.runtracker.helper.Constants.KEY_NAME
import com.maouni92.runtracker.helper.Constants.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    private  var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAccess = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(!isFirstAccess) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }


       binding.continueButton.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if(success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(requireView(), "Please fill all fields", Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = binding.nameField.text.toString()
        val weight = binding.weightField.text.toString()
        if(name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
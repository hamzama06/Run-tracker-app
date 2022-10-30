package com.maouni92.runtracker.ui.fragments


import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.maouni92.runtracker.R
import com.maouni92.runtracker.databinding.FragmentSettingsBinding
import com.maouni92.runtracker.databinding.FragmentSetupBinding
import com.maouni92.runtracker.helper.Constants.KEY_NAME
import com.maouni92.runtracker.helper.Constants.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private  var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadInfo()
        binding.submitButton.setOnClickListener {
            submitChanges()
        }
    }


    private fun loadInfo(){
        val name = sharedPreferences.getString(KEY_NAME, "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT, 75f)
        binding.nameSettingField.setText(name)
        binding.weightSettingsField.setText(weight.toString())
    }

    private fun submitChanges(){
        val name = binding.nameSettingField.text.toString()
        val weight =  binding.weightSettingsField.text.toString()

        if (name.isNotEmpty() && weight.isNotEmpty()){
            sharedPreferences.edit()
                .putString(KEY_NAME, name)
                .putFloat(KEY_WEIGHT, weight.toFloat())
                .apply()
            Snackbar.make(requireView(), "Changes saved", Snackbar.LENGTH_LONG).show()
        }else{
            Snackbar.make(requireView(), "Please fill out all the fields", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
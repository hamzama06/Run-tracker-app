package com.maouni92.runtracker.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.maouni92.runtracker.BuildConfig
import com.maouni92.runtracker.R
import com.maouni92.runtracker.adapters.RunAdapter
import com.maouni92.runtracker.data.Run
import com.maouni92.runtracker.databinding.ActivityMainBinding
import com.maouni92.runtracker.databinding.FragmentRunBinding
import dagger.hilt.android.AndroidEntryPoint
import com.maouni92.runtracker.helper.AppExtensions
import com.maouni92.runtracker.helper.AppExtensions.hasLocationPermissions
import com.maouni92.runtracker.helper.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.maouni92.runtracker.helper.SortType
import com.maouni92.runtracker.ui.viewModels.MainViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private  var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    private lateinit var runAdapter: RunAdapter
    private lateinit var spinner: Spinner
    private var runsList  = ArrayList<Run>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()

        spinner = binding.sortSpinner
        setupRecyclerView()
        setupSpinner()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> viewModel.sortRuns(SortType.DATE)
                    1 -> viewModel.sortRuns(SortType.RUNNING_TIME)
                    2 -> viewModel.sortRuns(SortType.DISTANCE)
                    3 -> viewModel.sortRuns(SortType.AVG_SPEED)
                    4 -> viewModel.sortRuns(SortType.CALORIES_BURNED)
                }
            }
        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {list->
            runsList = list as ArrayList<Run>
            runAdapter.submitList(list)
        })

        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }

        attachItemTouchHelperToRecyclerView()
    }

    private fun setupRecyclerView() = binding.runsRecyclerView.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun attachItemTouchHelperToRecyclerView(){
        ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
               return makeMovementFlags(0,ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                val deletedRun = runsList[position]
                viewModel.deleteRun(deletedRun)
                Snackbar.make(requireView(), "Run deleted ", Snackbar.LENGTH_LONG).show()

            }

        }).attachToRecyclerView(binding.runsRecyclerView)
    }

    private fun setupSpinner(){
        when(viewModel.sortType) {
            SortType.DATE -> spinner.setSelection(0)
            SortType.RUNNING_TIME -> spinner.setSelection(1)
            SortType.DISTANCE -> spinner.setSelection(2)
            SortType.AVG_SPEED -> spinner.setSelection(3)
            SortType.CALORIES_BURNED -> spinner.setSelection(4)
        }
    }

    private fun requestPermissions(){
        if (requireContext().hasLocationPermissions()){
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "Location permissions is required to use this app.",
                LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Location permissions is required to use this app.",
                LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
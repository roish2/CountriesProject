package com.example.countriesproject

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.example.countriesproject.ui.MainFragment
import androidx.lifecycle.ViewModelProviders
import com.example.countriesproject.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var progressBar: ProgressBar
    private val fragmentManager = supportFragmentManager
    private val BACKSTACK_NAME:String = "fragments"
    private val BACKSTACK_BORDERS_LIMIT:Int = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        progressBar = findViewById(R.id.progress_bar)

        viewModel.steps.observe(this, nameObserver)

        viewModel.getAllCountries()
    }

    private val nameObserver = Observer<MainViewModel.Steps> { step ->
        when (step) {
            is MainViewModel.Steps.DataReady -> {
                val newFragment: MainFragment = MainFragment.newInstance(step.data)
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.fragment_container, newFragment).commit()
                progressBar.visibility = View.GONE
            }
            is MainViewModel.Steps.BordersReady -> {
                val newFragment: MainFragment =
                    MainFragment.newInstance(step.data, step.currentCountry)
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, newFragment)
                    .addToBackStack(BACKSTACK_NAME).commit()
            }
            is MainViewModel.Steps.OnError -> {

            }
        }
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > BACKSTACK_BORDERS_LIMIT) {
            val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
            alertDialog.setMessage(getString(R.string.to_many_country_message))
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.back_to_last_country),
                DialogInterface.OnClickListener { dialog, which ->
                    super.onBackPressed()
                    dialog.dismiss() })
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.back_to_start),
                DialogInterface.OnClickListener { dialog, which ->
                    fragmentManager.popBackStack(BACKSTACK_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    dialog.dismiss() })
            alertDialog.show()

        } else {
            super.onBackPressed()
        }

    }
}
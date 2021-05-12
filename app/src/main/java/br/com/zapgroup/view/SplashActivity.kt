package br.com.zapgroup.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.zapgroup.databinding.ActivityMainBinding
import br.com.zapgroup.utils.Status
import br.com.zapgroup.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBd()
    }

    private fun setBd() {
        viewModel.setDB().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {

                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Err", Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        Toast.makeText(this, "load", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}
package br.com.zapgroup.view

import android.content.res.Resources
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.zapgroup.databinding.ActivityMainBinding
import br.com.zapgroup.utils.Connectivity.Companion.isConnected
import br.com.zapgroup.utils.Status
import br.com.zapgroup.utils.loadSnackBar
import br.com.zapgroup.viewmodel.SplashViewModel
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (isConnected(this)) {
            fetchLatestProperties()
        } else {
            getLocalSored()
        }
    }

    private fun getLocalSored() {
        try {
            viewModel.hasStoredObject()
            PropertyListActivity.open(this)
        } catch (exception: Resources.NotFoundException) {
            showReloadView()
        }
    }

    private fun showReloadView() {
        loadSnackBar(this, "Erro tente novamente")
        binding.run {
            tryAgainLoad.visibility = INVISIBLE
            tryAgainText.visibility = VISIBLE
            reloadButton.setOnClickListener {
                fetchLatestProperties()
            }
        }
    }

    private fun fetchLatestProperties() {
        viewModel.fetchLatestProperties().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        PropertyListActivity.open(this)
                    }
                    Status.ERROR -> {
                        getLocalSored()
                    }
                    Status.LOADING -> {
                        binding.run {
                            tryAgainLoad.visibility = VISIBLE
                            tryAgainText.visibility = INVISIBLE
                        }
                    }
                }
            }
        })
    }
}
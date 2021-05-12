package br.com.zapgroup.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.zapgroup.databinding.ActivityPropertyListBinding
import br.com.zapgroup.utils.Status
import br.com.zapgroup.view.adapter.ItemClick
import br.com.zapgroup.view.adapter.PropertyAdapter
import br.com.zapgroup.viewmodel.PropertyListViewModel
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class PropertyListActivity : AppCompatActivity(), ItemClick {

    private lateinit var binding: ActivityPropertyListBinding
    private val viewModel: PropertyListViewModel by viewModel()
    private val propertyAdapter by lazy { PropertyAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.environmentRecycler.adapter = propertyAdapter
        binding.buttonZap.setOnClickListener {
            fetchZapList()
        }
        binding.buttonViva.setOnClickListener {
            fetchVivaList()
        }
    }

    private fun fetchZapList() {
        viewModel.getZapPropertyList().observe(this, Observer {
            it?.let { resourceData ->
                when (resourceData.status) {
                    Status.SUCCESS -> {
                        propertyAdapter.setListView(resourceData.data ?: listOf(), "ZAP")
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

    private fun fetchVivaList() {
        viewModel.getVivaPropertyList().observe(this, Observer {
            it?.let { resourceData ->
                when (resourceData.status) {
                    Status.SUCCESS -> {
                        propertyAdapter.setListView(resourceData.data ?: listOf(), "VIVA")
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

    override fun onClickListener(id: String) {
        val i = Intent(this, PropertyDetailActivity::class.java)
        i.putExtra("EXTRA_ID", id)
        startActivity(i)
    }
}
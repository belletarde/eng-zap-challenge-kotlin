package br.com.zapgroup.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import br.com.zapgroup.R
import br.com.zapgroup.databinding.ActivityPropertyListBinding
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.Status
import br.com.zapgroup.utils.loadSnackBar
import br.com.zapgroup.view.adapter.ItemClick
import br.com.zapgroup.view.adapter.PropertyAdapter
import br.com.zapgroup.viewmodel.PropertyListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PropertyListActivity : AppCompatActivity(), ItemClick {

    private lateinit var binding: ActivityPropertyListBinding
    private val viewModel: PropertyListViewModel by viewModel()
    private val propertyAdapter by lazy { PropertyAdapter(this) }
    private val linearLayoutManager by lazy { LinearLayoutManager(this) }
    private var page = 1
    private var loading = false

    companion object {
        fun open(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, PropertyListActivity::class.java)
            appCompatActivity.startActivity(intent)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        fetchZapList(page)
        binding.run {
            buttonZap.setOnClickListener {
                page = 1
                fetchZapList(page)
                buttonViva.setBackgroundDrawable(resources.getDrawable(R.drawable.default_list_tab_bg));
                it.setBackgroundDrawable(resources.getDrawable(R.drawable.seleceted_list_tab_bg));
            }
            buttonViva.setOnClickListener {
                page = 1
                fetchVivaList(page)
                buttonZap.setBackgroundDrawable(resources.getDrawable(R.drawable.default_list_tab_bg));
                it.setBackgroundDrawable(resources.getDrawable(R.drawable.seleceted_list_tab_bg));
            }
        }
    }

    private fun initRecycler() {
        binding.run {
            propertyRecycler.layoutManager = linearLayoutManager
            propertyRecycler.adapter = propertyAdapter
            propertyRecycler.addOnScrollListener(scrollListener())
        }
    }

    private fun initRecyclerItems(list: List<PropertyResponse>) {
        binding.run {
            if(page > 1) {
                propertyAdapter.loadMore(list)
            } else {
                propertyAdapter.setListView(list)
                binding.propertyRecycler.smoothScrollToPosition(0)
            }
        }
    }

    private fun showErrorView(isListEmpty: Boolean = false) {
        binding.run {
            propertyRecycler.visibility = GONE
            propertyDetailErrorContainer.visibility = VISIBLE
            if(isListEmpty) {
                propertyDetailErrorLayout.errorMessageText.text = "Nada para ver aqui, essa lista está vazia"
                propertyDetailErrorLayout.reloadButton.visibility = GONE
            } else {
                propertyDetailErrorLayout.errorMessageText.text = "Ocorreu um erro na busca dos dados"
                propertyDetailErrorLayout.reloadButton.visibility = VISIBLE
                propertyDetailErrorLayout.reloadButton.setOnClickListener {
                    SplashActivity.open(this@PropertyListActivity)
                }
            }
        }
    }

    private fun fetchZapList(page: Int) {
        viewModel.getZapPropertyList(page).observe(this, Observer {
            it?.let { resourceData ->
                when (resourceData.status) {
                    Status.SUCCESS -> {
                        initRecyclerItems(resourceData.data ?: listOf())
                        loading = false
                    }
                    Status.ERROR -> {
                        loading = false
                        showErrorView()
                    }
                    Status.LOADING -> {
                        loading = true
                        if(page > 1) {
                            loadSnackBar(PropertyListActivity@this, "Carregando mais opções...")
                        }
                    }
                }
            }
        })
    }

    private fun fetchVivaList(page: Int) {
        viewModel.getVivaPropertyList(page).observe(this, Observer {
            it?.let { resourceData ->
                when (resourceData.status) {
                    Status.SUCCESS -> {
                        initRecyclerItems(resourceData.data ?: listOf())
                        loading = false
                    }
                    Status.ERROR -> {
                        loading = false
                        showErrorView()
                    }
                    Status.LOADING -> {
                        loading = true
                        if(page > 1) {
                            loadSnackBar(PropertyListActivity@this, "Carregando mais opções...")
                        }
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

    private fun scrollListener(): OnScrollListener {
        return object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            fetchVivaList(++page)
                            loading = true
                        }
                    }
                }
            }
        }
    }
}
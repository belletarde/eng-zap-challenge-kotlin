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
import br.com.zapgroup.data.FetchBusinessLogic.Companion.VIVA
import br.com.zapgroup.data.FetchBusinessLogic.Companion.ZAP
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
    private var type = ZAP

    companion object {
        private const val ERROR = "error"
        fun open(appCompatActivity: AppCompatActivity, error: Boolean = false) {
            val intent = Intent(appCompatActivity, PropertyListActivity::class.java)
            intent.putExtra(ERROR, error)
            appCompatActivity.startActivity(intent)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        fetchPropertyList(page)
        verifyExtra()
        setBtn()
    }

    private fun verifyExtra() {
        if(intent.getBooleanExtra(ERROR, false)) {
            loadSnackBar(this, getString(R.string.error_update_list))
        }
    }

    private fun setBtn() {
        binding.run {
            buttonZap.setOnClickListener {
                type = ZAP
                page = 1
                fetchPropertyList(page)
                buttonViva.setBtnNotClicked()
                buttonZap.setBtnClicked()
            }
            buttonViva.setOnClickListener {
                type = VIVA
                page = 1
                fetchPropertyList(page)
                buttonZap.setBtnNotClicked()
                buttonViva.setBtnClicked()
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
                propertyDetailErrorLayout.errorMessageText.text = getString(R.string.emptyList)
                propertyDetailErrorLayout.reloadButton.visibility = GONE
            } else {
                propertyDetailErrorLayout.errorMessageText.text = getString(R.string.load_data_error)
                propertyDetailErrorLayout.reloadButton.visibility = VISIBLE
                propertyDetailErrorLayout.reloadButton.setOnClickListener {
                    SplashActivity.open(this@PropertyListActivity)
                }
            }
        }
    }

    private fun fetchPropertyList(page: Int) {
        viewModel.getPropertyList(page, type).observe(this, Observer {
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
                            loadSnackBar(PropertyListActivity@this, getString(R.string.load_more))
                        }
                    }
                }
            }
        })
    }

    override fun onClickListener(id: String) {
        PropertyDetailActivity.open(this, id)
    }

    private fun scrollListener(): OnScrollListener {
        return object: OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            fetchPropertyList(++page)
                            loading = true
                        }
                    }
                }
            }
        }
    }
}
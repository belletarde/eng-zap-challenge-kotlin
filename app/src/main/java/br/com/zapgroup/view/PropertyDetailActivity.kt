package br.com.zapgroup.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.zapgroup.R
import br.com.zapgroup.data.FetchBusinessLogic.Companion.RENTAL
import br.com.zapgroup.databinding.ActivityPropertyDetailBinding
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.*
import br.com.zapgroup.view.adapter.PropertyPageAdapter
import br.com.zapgroup.viewmodel.PropertyDetailViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class PropertyDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityPropertyDetailBinding
    private val viewModel: PropertyDetailViewModel by viewModel()
    private var lat = 0.0
    private var lon = 0.0

    companion object {
        private const val EXTRA_ID = "extra_id"
        fun open(appCompatActivity: AppCompatActivity, id: String) {
            val intent = Intent(appCompatActivity, PropertyDetailActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            appCompatActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getById()
        bindBackBtn()
    }

    private fun bindBackBtn() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getById() {
        val id = intent.getStringExtra(EXTRA_ID) ?: ""

        viewModel.getById(id).observe(this, Observer {
            it?.let { resourceData ->
                when (resourceData.status) {
                    Status.SUCCESS -> {
                        resourceData.data?.let { it1 -> loadDetail(it1) }
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    private fun loadDetail(data: PropertyResponse) {
        binding.run {
            setGoogleMap(data.address.geoLocation.location.lat, data.address.geoLocation.location.lon)
            propertyImagePagerDetail.adapter = PropertyPageAdapter(data, this@PropertyDetailActivity)
            nextPagerDetail.addViewPage(propertyImagePagerDetail, data.images.size)
            scrollableLayout.requestDisallowInterceptTouchEvent(true)
            mapHomeButton.setOnClickListener { setGoogleMap(data.address.geoLocation.location.lat, data.address.geoLocation.location.lon) }
            PropertyDetail.fillData(
                data,
                this@PropertyDetailActivity,
                propertyAddressDetail,
                null,
                screenTitle,
                null,
                propertyDetailsDetail
            )

            var total = 0
            if(data.pricingInfos.monthlyCondoFee.isNumber()) {
                condofeeLayout.visibility = VISIBLE
                propertyCondofee.text = data.pricingInfos.monthlyCondoFee.setCurrency()
                total += data.pricingInfos.monthlyCondoFee.toInt()
            }

            if(data.pricingInfos.yearlyIptu.isNumber()) {
                iptuLayout.visibility = VISIBLE
                val iptuMonthly = (data.pricingInfos.yearlyIptu.toDouble() / 12).roundToInt()
                propertyIptuDetail.text = getString(R.string.property_IPTU_value_detail, iptuMonthly)
                total += iptuMonthly
            }

            if(data.pricingInfos.businessType == RENTAL) {
                propertyValueLabel.text = getString(R.string.rental_property)
                propertyTitle.text = getString(R.string.property_rental_title_detail, data.bedrooms, data.usableAreas)
                propertyPriceDetail.text = data.pricingInfos.rentalTotalPrice.setCurrency()
                total += data.pricingInfos.rentalTotalPrice.toInt()
            } else {
                propertyValueLabel.text = getString(R.string.property_value)
                propertyTitle.text = getString(R.string.property_sell_title_detail, data.bedrooms, data.usableAreas)
                propertyPriceDetail.text = data.pricingInfos.price.setCurrency()
                total += data.pricingInfos.price.toInt()
            }

            propertyTotalValueDetail.text = getString(R.string.property_total_value_detail, total)
            val diff = Date().time - data.updatedAt.toDate().time
            lastUpdateDate.text = getString(R.string.last_updated_time, TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))
        }
    }

    private fun setGoogleMap(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
       enableScrollInsideNestedLayout()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun enableScrollInsideNestedLayout() {
        binding.mapImage.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disallow ScrollView to intercept touch events.
                    binding.scrollableLayout.requestDisallowInterceptTouchEvent(true)
                    // Disable touch on transparent view
                    false
                }
                MotionEvent.ACTION_UP -> {
                    // Allow ScrollView to intercept touch events.
                    binding.scrollableLayout.requestDisallowInterceptTouchEvent(false)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    binding.scrollableLayout.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        // Add a marker in Sydney and move the camera
        val eventLocal = LatLng(lat, lon)
        googleMap.addMarker(MarkerOptions().position(eventLocal).title("Aqui"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocal, 16f))
    }
}
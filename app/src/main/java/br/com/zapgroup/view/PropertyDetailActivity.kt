package br.com.zapgroup.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
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
    var total = 0
    private lateinit var mapFragment: SupportMapFragment
    companion object {
        private val MAP_ZOOM = 16f
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

        viewModel.getById(id).observe(this, {
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
            scrollableLayout.requestDisallowInterceptTouchEvent(true)

            propertyImagePagerDetail.adapter = PropertyPageAdapter(data, this@PropertyDetailActivity)
            nextPagerDetail.addViewPage(propertyImagePagerDetail, data.images.size)

            showIPTU(data.pricingInfos.yearlyIptu)
            showMonthlyCondoFee(data.pricingInfos.monthlyCondoFee)
            showLastChangeTime(data.updatedAt)

            if(data.pricingInfos.businessType == RENTAL) {
                setRentalValues(data)
            } else {
                propertyPriceDetail?.text = data.pricingInfos.price.setCurrency()
                setSellValues()
            }
            PropertyDetailHelper.fillData(
                data,
                this@PropertyDetailActivity,
                propertyAddressDetail,
                propertyTitle,
                null,
                propertyDetailsDetail
            )
        }
    }

    private fun showLastChangeTime(updatedAt: String) {
        val diff = Date().time - updatedAt.toDate().time
        binding.lastUpdateDate.text = getString(R.string.last_updated_time, TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))
    }

    private fun setSellValues() {
        with(binding) {
            propertyValueLabel.text = getString(R.string.property_value)
            propertyTitle.text = getString(R.string.property_sell_title)
            totalLayout.visibility = GONE
        }
    }

    private fun setRentalValues(data: PropertyResponse) {
        with(binding) {
            propertyPriceDetail.text = getString(R.string.property_total_rental_value_detail, data.pricingInfos.price.setCurrency())
            propertyValueLabel.text = getString(R.string.rental_property)
            total += data.pricingInfos.price.toInt()
            propertyTitle.text = getString(R.string.property_rental_title)
            propertyTotalValueDetail.text = getString(R.string.property_total_rental_value_detail, total.setCurrency())
        }

    }

    private fun showMonthlyCondoFee(monthlyCondoFee: String) {
        with(binding) {
            if(monthlyCondoFee.isNumber()) {
                condofeeLayout.visibility = VISIBLE
                propertyCondofee.text = getString(R.string.property_total_rental_value_detail, monthlyCondoFee.setCurrency())
                total += monthlyCondoFee.toInt()
            }
        }
    }

    private fun showIPTU(yearlyIptu: String) {
        with(binding) {
            if(yearlyIptu.isNumber()) {
                iptuLayout.visibility = VISIBLE
                val iptuMonthly = (yearlyIptu.toDouble() / 12).roundToInt()
                propertyIptuDetail.text = getString(R.string.property_IPTU_value_detail, iptuMonthly)
                total += iptuMonthly
            }
        }
    }

    private fun setGoogleMap(lat: Double, lon: Double) {
        this.lat = lat
        this.lon = lon
        mapFragment= supportFragmentManager
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
        val eventLocal = LatLng(lat, lon)
        googleMap.addMarker(MarkerOptions().position(eventLocal).title(getString(R.string.marker_title)))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocal, MAP_ZOOM))
        setHomeButton(googleMap, eventLocal)
    }

    private fun setHomeButton(googleMap: GoogleMap, eventLocal: LatLng) {
        binding.mapHomeButton.setOnClickListener { googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocal, MAP_ZOOM)) }
        googleMap.setOnCameraMoveListener {
            val isSameLon = googleMap.cameraPosition.target.longitude == lon
            val isSameLat = googleMap.cameraPosition.target.latitude == lat
            binding.mapHomeButton.isInvisible = isSameLat && isSameLon
        }
    }
}

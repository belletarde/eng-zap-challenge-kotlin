package br.com.zapgroup.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.zapgroup.R
import br.com.zapgroup.data.FetchBusinessLogic.Companion.RENTAL
import br.com.zapgroup.databinding.ListItemBinding
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.setCurrency
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class PropertyViewHolder(
    private val itemClick: ItemClick,
    private val itemBinding: ListItemBinding
) : RecyclerView.ViewHolder(itemBinding.root){

    @SuppressLint("ResourceAsColor", "StringFormatMatches")
    fun bind(propertyResponse: PropertyResponse){
        with(propertyResponse) {
            with(itemBinding) {
                root.setOnClickListener {
                    itemClick.onClickListener(id)
                }
                propertyImagePager.adapter = PropertyPageAdapter(propertyResponse, itemBinding.root.context, itemClick)
                nextPager.addViewPage(propertyImagePager, images.size)
                propertyAddress.text = root.context.getString(R.string.property_address, address.neighborhood, address.city)
                propertyArea.text = root.context.getString(R.string.property_area, usableAreas)
                propertyDetails.addItems(
                    listOf(
                        root.context.getString(R.string.property_detail_bedrooms, bedrooms),
                        root.context.getString(R.string.property_detail_bathrooms, bathrooms),
                        root.context.getString(R.string.property_detail_parking, parkingSpaces)
                    )
                )

                if(pricingInfos.businessType == RENTAL) {
                    propertyType.text = root.context.getString(R.string.property_rental_title)
                    propertyPrice.text = pricingInfos.rentalTotalPrice.setCurrency()
                } else {
                    propertyType.text = root.context.getString(R.string.property_sell_title)
                    propertyPrice.text = pricingInfos.price.setCurrency()
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, itemClick: ItemClick): PropertyViewHolder {
            val itemBinding = ListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return PropertyViewHolder(
                itemClick,
                itemBinding
            )
        }
    }
}
package br.com.zapgroup.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View.GONE
import android.widget.TextView
import br.com.zapgroup.R
import br.com.zapgroup.data.FetchBusinessLogic
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.view.customView.PropertyPillView

class PropertyDetailHelper {

    companion object {
        @SuppressLint("StringFormatMatches")
        fun fillData(
            propertyResponse: PropertyResponse, context: Context,
            propertyAddress: TextView,
            propertyType: TextView,
            propertyPrice: TextView?,
            propertyDetails: PropertyPillView) {
            with(propertyResponse,
            ) {
                if(address.neighborhood.isNotEmpty() && address.city.isNotEmpty()) {
                    propertyAddress.text = context.getString(R.string.property_address, address.neighborhood, address.city)
                } else {
                    propertyAddress.visibility = GONE
                }
                propertyDetails.addItems(
                    listOf(
                        context.getString(R.string.property_detail_bedrooms, bedrooms),
                        context.getString(R.string.property_detail_bathrooms, bathrooms),
                        context.getString(R.string.property_detail_parking, parkingSpaces)
                    )
                )

                if(pricingInfos.businessType == FetchBusinessLogic.RENTAL) {
                    propertyType.text = context.getString(R.string.property_rental_title_detail, bedrooms, usableAreas)
                    propertyPrice?.text = context.getString(R.string.property_total_rental_value_detail, pricingInfos.rentalTotalPrice.setCurrency())
                } else {
                    propertyType.text = context.getString(R.string.property_sell_title_detail, bedrooms, usableAreas)
                    propertyPrice?.text = pricingInfos.price.setCurrency()
                }
            }

        }
    }
}
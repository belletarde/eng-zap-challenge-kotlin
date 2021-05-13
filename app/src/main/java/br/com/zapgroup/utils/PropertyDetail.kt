package br.com.zapgroup.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import br.com.zapgroup.R
import br.com.zapgroup.data.FetchBusinessLogic
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.view.customView.PropertyPillView

class PropertyDetail {

    companion object {
        @SuppressLint("StringFormatMatches")
        fun fillData(
            propertyResponse: PropertyResponse, context: Context,
            propertyAddress: TextView,
            propertyArea: TextView?,
            propertyType: TextView,
            propertyPrice: TextView?,
            propertyDetails: PropertyPillView) {
            with(propertyResponse,
            ) {
                propertyAddress.text = context.getString(R.string.property_address, address.neighborhood, address.city)
                propertyArea?.text = context.getString(R.string.property_area, usableAreas)
                propertyDetails.addItems(
                    listOf(
                        context.getString(R.string.property_detail_bedrooms, bedrooms),
                        context.getString(R.string.property_detail_bathrooms, bathrooms),
                        context.getString(R.string.property_detail_parking, parkingSpaces)
                    )
                )

                if(pricingInfos.businessType == FetchBusinessLogic.RENTAL) {
                    propertyType.text = context.getString(R.string.property_rental_title)
                    propertyPrice?.text = pricingInfos.rentalTotalPrice.setCurrency()
                } else {
                    propertyType.text = context.getString(R.string.property_sell_title)
                    propertyPrice?.text = pricingInfos.price.setCurrency()
                }
            }

        }
    }
}
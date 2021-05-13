package br.com.zapgroup.data

import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.isNotNumber
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode

class FetchBusinessLogic {
    companion object {
        private const val RENTAL = "RENTAL"
        private const val SALE = "SALE"
        private const val VIVA = "VIVA"
        private const val ZAP = "ZAP"

        private const val minlon = -46.693419
        private const val minlat = -23.568704
        private const val maxlon = -46.641146
        private const val maxlat = -23.546686

        private val VIVA_BOUNDING_BOX = BigDecimal(1.5)
        private val ZAP_BOUNDING_BOX = BigDecimal(0.9)
        private val VALUE_PER_METER = BigDecimal(3500)
        private val CONDO_FEE_PERCENT = BigDecimal(0.3)
        private val RENTAL_MAX_VIVA_PRICE = BigDecimal(4000)
        private val SALE_MAX_VIVA_PRICE = BigDecimal(700000)
        private val RENTAL_MIN_ZAP_PRICE = BigDecimal(3500)
        private val SALE_MIN_ZAP_PRICE = BigDecimal(600000)

        fun getVivaLogic(prop: PropertyResponse): Boolean {
            try {
                with(prop) {
                    if(generalLogic(prop)) {
                        return false
                    }

                    if(pricingInfos.businessType == SALE) {
                        val salePrice = BigDecimal(pricingInfos.price)
                        if(salePrice > SALE_MAX_VIVA_PRICE) {
                            return false
                        }
                    }

                    if (pricingInfos.businessType == RENTAL) {
                        val rentalPrice = BigDecimal(pricingInfos.rentalTotalPrice)
                        if(rentalPrice > boundingBoxValue(prop, RENTAL_MAX_VIVA_PRICE, VIVA)) {
                            return false
                        }

                        val monthlyCondoFee = BigDecimal(pricingInfos.monthlyCondoFee)
                        val maxValueToCondoFee =
                            BigDecimal(pricingInfos.rentalTotalPrice).multiply(CONDO_FEE_PERCENT)
                        if (monthlyCondoFee >= maxValueToCondoFee) {
                            return false
                        }
                    }
                    return true
                }
            } catch (e: Exception) {
                throw e
            }
        }

        fun getZapLogic(prop: PropertyResponse): Boolean {
            with(prop) {
                if(generalLogic(prop)) {
                    return false
                }

                if(pricingInfos.businessType == SALE) {
                    val salePrice = BigDecimal(pricingInfos.price)
                    if(salePrice < boundingBoxValue(prop, SALE_MIN_ZAP_PRICE, ZAP)) {
                        return false
                    }

                    val pricePerMeter = BigDecimal(pricingInfos.price).divide(BigDecimal(usableAreas), 2, RoundingMode.HALF_DOWN)
                    if (pricePerMeter <= VALUE_PER_METER) {
                        return false
                    }
                }

                if(pricingInfos.businessType == RENTAL) {
                    val rentalPrice = BigDecimal(pricingInfos.rentalTotalPrice)
                    if(rentalPrice < RENTAL_MIN_ZAP_PRICE) {
                        return true
                    }
                }
            }
            return true
        }

        private fun generalLogic(prop: PropertyResponse): Boolean {
            with(prop) {
                if (address.geoLocation.location.lat == 0.0 && address.geoLocation.location.lon == 0.0) {
                    return true
                }

                if(pricingInfos.monthlyCondoFee.isNotNumber()) {
                    return true
                }

                if(usableAreas <= 0) {
                    return true
                }
            }

            return false
        }

        private fun boundingBoxSale(prop: PropertyResponse): BigDecimal {
            with(prop.address.geoLocation.location) {
                if((lon in minlon..maxlon) && (lat in minlat..maxlat)) {

                }
            }
            return SALE_MIN_ZAP_PRICE
        }


        private fun boundingBoxValue(prop: PropertyResponse, value: BigDecimal, type: String): BigDecimal {
            val lon = prop.address.geoLocation.location.lon
            val lat = prop.address.geoLocation.location.lat

            if((lon in minlon..maxlon) && (lat in minlat..maxlat)) {
                when(type) {
                    RENTAL -> {
                        return RENTAL_MAX_VIVA_PRICE.multiply(VIVA_BOUNDING_BOX)
                    }
                    SALE -> {
                        return SALE_MIN_ZAP_PRICE.multiply(ZAP_BOUNDING_BOX)
                    }
                }
            }

            return value
        }
    }
}
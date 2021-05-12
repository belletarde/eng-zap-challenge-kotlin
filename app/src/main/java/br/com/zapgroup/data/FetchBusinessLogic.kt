package br.com.zapgroup.data

import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.isNotNumber
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode

class FetchBusinessLogic {
    companion object {
        private const val minlon = -46.693419
        private const val minlat = -23.568704
        private const val maxlon = -46.641146
        private const val maxlat = -23.546686
        private const val RENTAL = "RENTAL"
        private const val SALE = "SALE"
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
                        if(rentalPrice > RENTAL_MAX_VIVA_PRICE) {
                            return true
                        }

                        val monthlyCondoFee = BigDecimal(pricingInfos.monthlyCondoFee)
                        val maxValueToCondoFee =
                            BigDecimal(pricingInfos.rentalTotalPrice).multiply(CONDO_FEE_PERCENT)
                        if (monthlyCondoFee >= maxValueToCondoFee) {
                            return false
                        }
                    }
                }
            } catch (e: Exception) {
                throw e
            }


            return true
        }

        fun getZapLogic(prop: PropertyResponse): Boolean {
            with(prop) {
                if(generalLogic(prop)) {
                    return false
                }

                if(pricingInfos.businessType == SALE) {
                    val salePrice = BigDecimal(pricingInfos.price)
                    if(salePrice < SALE_MIN_ZAP_PRICE) {
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

        fun boundingBox(prop: PropertyResponse): Boolean {
            with(prop.address.geoLocation.location) {
                if((lon in minlon..maxlon) && (lat in minlat..maxlat)) {
                    return true
                }
            }
            return false
        }
    }
}
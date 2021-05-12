package br.com.zapgroup.model.api

data class PricingInfosResponse(
    val period: String,
    val yearlyIptu: String,
    val price: String,
    val rentalTotalPrice: String,
    val businessType: String,
    val monthlyCondoFee: String
)

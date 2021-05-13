package br.com.zapgroup.model.api

data class PropertyResponse(
    val usableAreas: Int,
    val listingType: String,
    val createdAt: String,
    val listingStatus: String,
    val id: String,
    val parkingSpaces: String,
    val updatedAt: String,
    val owner: Boolean,
    val images: List<String>,
    val address: PropertyAddressResponse,
    val bathrooms: Int,
    val bedrooms: Int,
    val pricingInfos: PricingInfosResponse
) {

}
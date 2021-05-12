package br.com.zapgroup.model.api


data class PropertyAddressResponse(
    val city: String,
    val neighborhood: String,
    val geoLocation: GeoLocationResponse
)
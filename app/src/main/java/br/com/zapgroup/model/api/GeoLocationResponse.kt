package br.com.zapgroup.model.api

data class GeoLocationResponse(
    val precision: String,
    val location: PropertyLocationResponse
)
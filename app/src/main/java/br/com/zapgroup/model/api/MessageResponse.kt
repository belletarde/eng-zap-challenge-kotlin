package br.com.zapgroup.model.api

import java.lang.Exception

data class MessageResponse(
    val success: Boolean,
    val exception: Exception?
)

data class PropertyShared(
    val list: List<PropertyResponse>
)
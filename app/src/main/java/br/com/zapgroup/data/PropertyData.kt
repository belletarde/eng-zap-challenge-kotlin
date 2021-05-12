package br.com.zapgroup.data

import br.com.zapgroup.model.api.PropertyResponse

interface PropertyData {
    fun setPropertyObjectString(propertyShared: String) : Boolean
    fun getPropertyObjectString() : String
    fun setPropertyStored() : Boolean
    fun isPropertyStored() : Boolean
    fun getVivaList(): List<PropertyResponse>
    fun getZapList(): List<PropertyResponse>
    fun getById(id: String): PropertyResponse
}


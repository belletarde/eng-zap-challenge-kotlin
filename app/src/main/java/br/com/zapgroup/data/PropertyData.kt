package br.com.zapgroup.data

import br.com.zapgroup.model.api.PropertyResponse

interface PropertyData {
    fun setPropertyObjectString(propertyShared: String) : Boolean
    fun getPropertyObjectString() : String
    fun setPropertyStored() : Boolean
    fun isPropertyStored() : Boolean
    fun getVivaList(page: Int = 1): List<PropertyResponse>
    fun getZapList(page: Int = 1): List<PropertyResponse>
    fun getById(id: String): PropertyResponse
    fun hasStoredObject(): Boolean
}


package br.com.zapgroup.repository

import br.com.zapgroup.model.api.PropertyResponse

interface SplashRepository {
    suspend fun getList(): List<PropertyResponse>
    fun addPropertyToTable(propertyList: String) : Boolean
    fun hasStoredObject(): Boolean
}
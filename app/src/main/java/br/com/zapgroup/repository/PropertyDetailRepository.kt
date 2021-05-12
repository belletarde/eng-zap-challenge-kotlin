package br.com.zapgroup.repository

import br.com.zapgroup.model.api.PropertyResponse

interface PropertyDetailRepository {
    suspend fun getPropertyDetail(id: String): PropertyResponse
}
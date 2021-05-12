package br.com.zapgroup.api


import br.com.zapgroup.data.AppSharedPreferences
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.repository.PropertyDetailRepository

class PropertyDetailApi(private val shared: AppSharedPreferences) : PropertyDetailRepository {

    override suspend fun getPropertyDetail(id: String): PropertyResponse {
        return shared.getById(id)
    }
}
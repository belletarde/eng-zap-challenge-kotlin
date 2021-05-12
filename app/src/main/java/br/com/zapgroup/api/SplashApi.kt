package br.com.zapgroup.api


import br.com.zapgroup.data.AppSharedPreferences
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.repository.SplashRepository

class SplashApi(private val apiService: ApiService, private val shared: AppSharedPreferences) : SplashRepository {

    override suspend fun getList(): List<PropertyResponse> {
        return apiService.getAll()
    }

    override fun addPropertyToTable(propertyList: String): Boolean {
        return shared.setPropertyObjectString(propertyList)
    }
}
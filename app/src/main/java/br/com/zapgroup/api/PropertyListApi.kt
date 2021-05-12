package br.com.zapgroup.api


import br.com.zapgroup.data.AppSharedPreferences
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.repository.PropertyListRepository

class PropertyListApi(private val shared: AppSharedPreferences) : PropertyListRepository {

    override fun getVivaList(): List<PropertyResponse> {
        return shared.getVivaList()
    }

    override fun getZapList(): List<PropertyResponse> {
        return shared.getZapList()
    }

}
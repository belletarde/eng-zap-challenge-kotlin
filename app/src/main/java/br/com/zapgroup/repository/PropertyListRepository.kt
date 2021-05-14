package br.com.zapgroup.repository

import br.com.zapgroup.model.api.PropertyResponse

interface PropertyListRepository {
    fun getVivaList(page: Int = 1): List<PropertyResponse>

    fun getZapList(page: Int = 1): List<PropertyResponse>
}
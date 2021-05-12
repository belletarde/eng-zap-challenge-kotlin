package br.com.zapgroup.repository

import br.com.zapgroup.model.api.PropertyResponse

interface PropertyListRepository {
    fun getVivaList(): List<PropertyResponse>

    fun getZapList(): List<PropertyResponse>
}
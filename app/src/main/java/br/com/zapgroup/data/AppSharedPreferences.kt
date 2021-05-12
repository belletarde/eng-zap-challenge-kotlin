package br.com.zapgroup.data

import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import br.com.zapgroup.data.SharedValues.Companion.PROPERTY_OBJECT
import br.com.zapgroup.data.SharedValues.Companion.PROPERTY_STORED
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.isNotNumber
import br.com.zapgroup.utils.toPropertyResponse
import java.math.BigDecimal
import kotlin.math.log

class AppSharedPreferences(
    private val sharedPreferences: SharedPreferences,
    private val editor : SharedPreferences.Editor) : PropertyData {

    override fun setPropertyObjectString(propertyShared: String): Boolean {
        return editor.putString(PROPERTY_OBJECT, propertyShared).commit()
    }

    override fun getPropertyObjectString(): String {
        return sharedPreferences.getString(PROPERTY_OBJECT, "") ?: throw Exception()
    }

    override fun setPropertyStored(): Boolean {
        return editor.putBoolean(PROPERTY_STORED, false).commit()
    }

    override fun isPropertyStored(): Boolean {
        return sharedPreferences.getBoolean(PROPERTY_STORED, false)
    }

    override fun hasStoredObject(): Boolean {
        return getPropertyObjectString().toPropertyResponse()?.list?.isNotEmpty() ?: throw Resources.NotFoundException()
    }

    override fun getVivaList(): List<PropertyResponse> {
        return getPropertyObjectString().toPropertyResponse()?.list?.filter { it ->
            FetchBusinessLogic.getVivaLogic(it)
        } ?: throw Resources.NotFoundException()
    }

    override fun getZapList(): List<PropertyResponse> {
        return getPropertyObjectString().toPropertyResponse()?.list?.filter { it ->
            FetchBusinessLogic.getZapLogic(it)
        } ?: throw Resources.NotFoundException()
    }

    override fun getById(id: String): PropertyResponse {
        return getPropertyObjectString().toPropertyResponse()?.list?.find { it.id == id } ?: throw Resources.NotFoundException()
    }
}
package br.com.zapgroup.data

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import br.com.zapgroup.data.SharedValues.Companion.PROPERTY_OBJECT
import br.com.zapgroup.data.SharedValues.Companion.PROPERTY_STORED
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.pagination
import br.com.zapgroup.utils.toDate
import br.com.zapgroup.utils.toPropertyResponse
import java.text.SimpleDateFormat
import java.time.Instant

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

    override fun getVivaList(page: Int): List<PropertyResponse> {
        val popList = getPropertyObjectString().toPropertyResponse()?.list?.filter { it ->
            FetchBusinessLogic.getVivaLogic(it)
        }?.sortedBy { it -> it.updatedAt.toDate() } ?: throw Resources.NotFoundException()

        return popList.pagination(page)
    }

    override fun getZapList(page: Int): List<PropertyResponse> {
        val popList = getPropertyObjectString().toPropertyResponse()?.list?.filter { it ->
            FetchBusinessLogic.getZapLogic(it)
        }?.sortedBy { it -> it.updatedAt.toDate() } ?: throw Resources.NotFoundException()

        return popList.pagination(page)
    }

    override fun getById(id: String): PropertyResponse {
        return getPropertyObjectString().toPropertyResponse()?.list?.find { it.id == id } ?: throw Resources.NotFoundException()
    }
}
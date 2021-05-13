package br.com.zapgroup.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.com.zapgroup.repository.PropertyListRepository
import br.com.zapgroup.repository.SplashRepository
import br.com.zapgroup.utils.Resource
import kotlinx.coroutines.Dispatchers

class PropertyListViewModel(private val repository: PropertyListRepository): ViewModel() {

    fun getZapPropertyList(page: Int = 1) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getZapList(page)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getVivaPropertyList(page: Int = 1) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getVivaList(page)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}
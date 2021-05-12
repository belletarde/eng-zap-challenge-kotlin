package br.com.zapgroup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.com.zapgroup.repository.PropertyListRepository
import br.com.zapgroup.repository.SplashRepository
import br.com.zapgroup.utils.Resource
import kotlinx.coroutines.Dispatchers

class PropertyListViewModel(private val repository: PropertyListRepository): ViewModel() {

    fun getZapPropertyList() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getZapList()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getVivaPropertyList() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getVivaList()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}
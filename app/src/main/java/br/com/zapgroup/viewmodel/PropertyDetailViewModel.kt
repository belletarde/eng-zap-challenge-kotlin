package br.com.zapgroup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.com.zapgroup.repository.PropertyDetailRepository
import br.com.zapgroup.repository.PropertyListRepository
import br.com.zapgroup.repository.SplashRepository
import br.com.zapgroup.utils.Resource
import kotlinx.coroutines.Dispatchers

class PropertyDetailViewModel(private val repository: PropertyDetailRepository): ViewModel() {

    fun getById(id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getPropertyDetail(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}
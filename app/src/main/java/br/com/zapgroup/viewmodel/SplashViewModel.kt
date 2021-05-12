package br.com.zapgroup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.com.zapgroup.model.api.PropertyShared
import br.com.zapgroup.repository.SplashRepository
import br.com.zapgroup.utils.Resource
import br.com.zapgroup.utils.objectToString
import kotlinx.coroutines.Dispatchers

class SplashViewModel(private val repository: SplashRepository): ViewModel() {
    fun setDB() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = repository.addPropertyToTable(
                        PropertyShared(
                            repository.getList()
                        ).objectToString()
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}
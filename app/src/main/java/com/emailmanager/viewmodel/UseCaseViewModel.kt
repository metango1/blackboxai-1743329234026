package com.emailmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emailmanager.data.AppDatabase
import com.emailmanager.data.entity.UseCase
import com.emailmanager.repository.UseCaseRepository
import kotlinx.coroutines.launch

class UseCaseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UseCaseRepository
    val allUseCases: LiveData<List<UseCase>>
    private val _operationStatus = MutableLiveData<OperationStatus>()
    val operationStatus: LiveData<OperationStatus> = _operationStatus

    init {
        val useCaseDao = AppDatabase.getDatabase(application).useCaseDao()
        repository = UseCaseRepository(useCaseDao)
        allUseCases = repository.allUseCases
    }

    fun insertUseCase(useCaseName: String, description: String?) = viewModelScope.launch {
        try {
            val useCase = UseCase(
                usecaseName = useCaseName,
                description = description
            )
            val useCaseId = repository.insertUseCase(useCase)
            _operationStatus.value = OperationStatus.Success("Use case added successfully with ID: $useCaseId")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to add use case")
        }
    }

    fun updateUseCase(useCase: UseCase) = viewModelScope.launch {
        try {
            repository.updateUseCase(useCase)
            _operationStatus.value = OperationStatus.Success("Use case updated successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to update use case")
        }
    }

    fun deleteUseCase(useCase: UseCase) = viewModelScope.launch {
        try {
            repository.deleteUseCase(useCase)
            _operationStatus.value = OperationStatus.Success("Use case deleted successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to delete use case")
        }
    }

    fun searchUseCases(query: String): LiveData<List<UseCase>> = repository.searchUseCases(query)

    fun getUseCaseById(useCaseId: Int): LiveData<UseCase> = repository.getUseCaseById(useCaseId)

    fun deleteAllUseCases() = viewModelScope.launch {
        try {
            repository.deleteAllUseCases()
            _operationStatus.value = OperationStatus.Success("All use cases deleted successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to delete all use cases")
        }
    }

    fun getUseCasesForEmail(emailId: String): LiveData<List<UseCase>> = 
        repository.getUseCasesForEmail(emailId)

    fun removeAllUseCasesFromEmail(emailId: String) = viewModelScope.launch {
        try {
            repository.removeAllUseCasesFromEmail(emailId)
            _operationStatus.value = OperationStatus.Success("All use cases removed from email successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to remove use cases from email")
        }
    }

    fun validateUseCaseInput(useCaseName: String): Boolean {
        return useCaseName.isNotBlank()
    }
}
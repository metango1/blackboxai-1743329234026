package com.emailmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emailmanager.data.AppDatabase
import com.emailmanager.data.entity.Email
import com.emailmanager.data.entity.Tag
import com.emailmanager.data.entity.BrowserGroup
import com.emailmanager.data.entity.UseCase
import com.emailmanager.repository.EmailRepository
import kotlinx.coroutines.launch
import java.util.Date

class EmailViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository: EmailRepository
    
    val allEmails: LiveData<List<Email>>
    private val _operationStatus = MutableLiveData<OperationStatus>()
    val operationStatus: LiveData<OperationStatus> = _operationStatus

    init {
        repository = EmailRepository(
            database.emailDao(),
            database.tagDao(),
            database.browserGroupDao(),
            database.useCaseDao()
        )
        allEmails = repository.allEmails
    }

    fun insertEmail(
        emailId: String,
        firstName: String,
        lastName: String,
        tabGroup: String
    ) = viewModelScope.launch {
        try {
            val email = Email(
                emailId = emailId,
                firstName = firstName,
                lastName = lastName,
                tabGroup = tabGroup,
                createdAt = Date(),
                updatedAt = Date()
            )
            repository.insertEmail(email)
            _operationStatus.value = OperationStatus.Success("Email added successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to add email")
        }
    }

    fun updateEmail(email: Email) = viewModelScope.launch {
        try {
            repository.updateEmail(email)
            _operationStatus.value = OperationStatus.Success("Email updated successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to update email")
        }
    }

    fun deleteEmail(email: Email) = viewModelScope.launch {
        try {
            repository.deleteEmail(email)
            _operationStatus.value = OperationStatus.Success("Email deleted successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to delete email")
        }
    }

    fun searchEmails(query: String): LiveData<List<Email>> = repository.searchEmails(query)

    // Tag operations
    fun getTagsForEmail(emailId: String): LiveData<List<Tag>> = repository.getTagsForEmail(emailId)

    fun addTagToEmail(emailId: String, tagId: Int) = viewModelScope.launch {
        try {
            repository.addTagToEmail(emailId, tagId)
            _operationStatus.value = OperationStatus.Success("Tag added successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to add tag")
        }
    }

    // Browser group operations
    fun getBrowserGroupsForEmail(emailId: String): LiveData<List<BrowserGroup>> = 
        repository.getBrowserGroupsForEmail(emailId)

    fun addBrowserGroupToEmail(emailId: String, browserGroupId: Int) = viewModelScope.launch {
        try {
            repository.addBrowserGroupToEmail(emailId, browserGroupId)
            _operationStatus.value = OperationStatus.Success("Browser group added successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to add browser group")
        }
    }

    // Use case operations
    fun getUseCasesForEmail(emailId: String): LiveData<List<UseCase>> = 
        repository.getUseCasesForEmail(emailId)

    fun addUseCaseToEmail(emailId: String, useCaseId: Int) = viewModelScope.launch {
        try {
            repository.addUseCaseToEmail(emailId, useCaseId)
            _operationStatus.value = OperationStatus.Success("Use case added successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to add use case")
        }
    }

    // Clear all relationships
    fun clearEmailRelations(emailId: String) = viewModelScope.launch {
        try {
            repository.clearEmailRelations(emailId)
            _operationStatus.value = OperationStatus.Success("Email relations cleared successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to clear email relations")
        }
    }
}

sealed class OperationStatus {
    data class Success(val message: String) : OperationStatus()
    data class Error(val message: String) : OperationStatus()
}
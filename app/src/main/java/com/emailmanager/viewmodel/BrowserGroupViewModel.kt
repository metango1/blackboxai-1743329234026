package com.emailmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emailmanager.data.AppDatabase
import com.emailmanager.data.entity.BrowserGroup
import com.emailmanager.repository.BrowserGroupRepository
import kotlinx.coroutines.launch

class BrowserGroupViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BrowserGroupRepository
    val allBrowserGroups: LiveData<List<BrowserGroup>>
    private val _operationStatus = MutableLiveData<OperationStatus>()
    val operationStatus: LiveData<OperationStatus> = _operationStatus

    init {
        val browserGroupDao = AppDatabase.getDatabase(application).browserGroupDao()
        repository = BrowserGroupRepository(browserGroupDao)
        allBrowserGroups = repository.allBrowserGroups
    }

    fun insertBrowserGroup(browserGroupName: String, description: String?) = viewModelScope.launch {
        try {
            val browserGroup = BrowserGroup(
                browserGroupName = browserGroupName,
                description = description
            )
            val browserGroupId = repository.insertBrowserGroup(browserGroup)
            _operationStatus.value = OperationStatus.Success("Browser group added successfully with ID: $browserGroupId")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to add browser group")
        }
    }

    fun updateBrowserGroup(browserGroup: BrowserGroup) = viewModelScope.launch {
        try {
            repository.updateBrowserGroup(browserGroup)
            _operationStatus.value = OperationStatus.Success("Browser group updated successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to update browser group")
        }
    }

    fun deleteBrowserGroup(browserGroup: BrowserGroup) = viewModelScope.launch {
        try {
            repository.deleteBrowserGroup(browserGroup)
            _operationStatus.value = OperationStatus.Success("Browser group deleted successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to delete browser group")
        }
    }

    fun searchBrowserGroups(query: String): LiveData<List<BrowserGroup>> = 
        repository.searchBrowserGroups(query)

    fun getBrowserGroupById(browserGroupId: Int): LiveData<BrowserGroup> = 
        repository.getBrowserGroupById(browserGroupId)

    fun deleteAllBrowserGroups() = viewModelScope.launch {
        try {
            repository.deleteAllBrowserGroups()
            _operationStatus.value = OperationStatus.Success("All browser groups deleted successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to delete all browser groups")
        }
    }

    fun getBrowserGroupsForEmail(emailId: String): LiveData<List<BrowserGroup>> = 
        repository.getBrowserGroupsForEmail(emailId)

    fun removeAllBrowserGroupsFromEmail(emailId: String) = viewModelScope.launch {
        try {
            repository.removeAllBrowserGroupsFromEmail(emailId)
            _operationStatus.value = OperationStatus.Success("All browser groups removed from email successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to remove browser groups from email")
        }
    }

    fun validateBrowserGroupInput(browserGroupName: String): Boolean {
        return browserGroupName.isNotBlank()
    }
}
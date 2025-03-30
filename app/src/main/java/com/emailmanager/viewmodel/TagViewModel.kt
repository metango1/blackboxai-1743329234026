package com.emailmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emailmanager.data.AppDatabase
import com.emailmanager.data.entity.Tag
import com.emailmanager.repository.TagRepository
import kotlinx.coroutines.launch

class TagViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TagRepository
    val allTags: LiveData<List<Tag>>
    private val _operationStatus = MutableLiveData<OperationStatus>()
    val operationStatus: LiveData<OperationStatus> = _operationStatus

    init {
        val tagDao = AppDatabase.getDatabase(application).tagDao()
        repository = TagRepository(tagDao)
        allTags = repository.allTags
    }

    fun insertTag(tagName: String, description: String?) = viewModelScope.launch {
        try {
            val tag = Tag(
                tagName = tagName,
                description = description
            )
            val tagId = repository.insertTag(tag)
            _operationStatus.value = OperationStatus.Success("Tag added successfully with ID: $tagId")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to add tag")
        }
    }

    fun updateTag(tag: Tag) = viewModelScope.launch {
        try {
            repository.updateTag(tag)
            _operationStatus.value = OperationStatus.Success("Tag updated successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to update tag")
        }
    }

    fun deleteTag(tag: Tag) = viewModelScope.launch {
        try {
            repository.deleteTag(tag)
            _operationStatus.value = OperationStatus.Success("Tag deleted successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to delete tag")
        }
    }

    fun searchTags(query: String): LiveData<List<Tag>> = repository.searchTags(query)

    fun getTagById(tagId: Int): LiveData<Tag> = repository.getTagById(tagId)

    fun deleteAllTags() = viewModelScope.launch {
        try {
            repository.deleteAllTags()
            _operationStatus.value = OperationStatus.Success("All tags deleted successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to delete all tags")
        }
    }

    fun getTagsForEmail(emailId: String): LiveData<List<Tag>> = repository.getTagsForEmail(emailId)

    fun removeAllTagsFromEmail(emailId: String) = viewModelScope.launch {
        try {
            repository.removeAllTagsFromEmail(emailId)
            _operationStatus.value = OperationStatus.Success("All tags removed from email successfully")
        } catch (e: Exception) {
            _operationStatus.value = OperationStatus.Error(e.message ?: "Failed to remove tags from email")
        }
    }

    fun validateTagInput(tagName: String): Boolean {
        return tagName.isNotBlank()
    }
}
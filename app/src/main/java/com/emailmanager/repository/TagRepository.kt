package com.emailmanager.repository

import androidx.lifecycle.LiveData
import com.emailmanager.data.dao.TagDao
import com.emailmanager.data.entity.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TagRepository(private val tagDao: TagDao) {
    // Basic tag operations
    val allTags: LiveData<List<Tag>> = tagDao.getAllTags()

    suspend fun insertTag(tag: Tag): Long = withContext(Dispatchers.IO) {
        tagDao.insertTag(tag)
    }

    suspend fun updateTag(tag: Tag) = withContext(Dispatchers.IO) {
        tagDao.updateTag(tag)
    }

    suspend fun deleteTag(tag: Tag) = withContext(Dispatchers.IO) {
        tagDao.deleteTag(tag)
    }

    fun searchTags(query: String): LiveData<List<Tag>> = tagDao.searchTags(query)

    fun getTagById(tagId: Int): LiveData<Tag> = tagDao.getTagById(tagId)

    // Batch operations
    suspend fun deleteAllTags() = withContext(Dispatchers.IO) {
        tagDao.deleteAllTags()
    }

    // Email relationship operations
    fun getTagsForEmail(emailId: String): LiveData<List<Tag>> = tagDao.getTagsForEmail(emailId)

    suspend fun removeAllTagsFromEmail(emailId: String) = withContext(Dispatchers.IO) {
        tagDao.removeAllTagsFromEmail(emailId)
    }
}
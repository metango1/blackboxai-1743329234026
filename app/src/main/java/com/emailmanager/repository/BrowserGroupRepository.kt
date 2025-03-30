package com.emailmanager.repository

import androidx.lifecycle.LiveData
import com.emailmanager.data.dao.BrowserGroupDao
import com.emailmanager.data.entity.BrowserGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BrowserGroupRepository(private val browserGroupDao: BrowserGroupDao) {
    // Basic browser group operations
    val allBrowserGroups: LiveData<List<BrowserGroup>> = browserGroupDao.getAllBrowserGroups()

    suspend fun insertBrowserGroup(browserGroup: BrowserGroup): Long = withContext(Dispatchers.IO) {
        browserGroupDao.insertBrowserGroup(browserGroup)
    }

    suspend fun updateBrowserGroup(browserGroup: BrowserGroup) = withContext(Dispatchers.IO) {
        browserGroupDao.updateBrowserGroup(browserGroup)
    }

    suspend fun deleteBrowserGroup(browserGroup: BrowserGroup) = withContext(Dispatchers.IO) {
        browserGroupDao.deleteBrowserGroup(browserGroup)
    }

    fun searchBrowserGroups(query: String): LiveData<List<BrowserGroup>> = 
        browserGroupDao.searchBrowserGroups(query)

    fun getBrowserGroupById(browserGroupId: Int): LiveData<BrowserGroup> = 
        browserGroupDao.getBrowserGroupById(browserGroupId)

    // Batch operations
    suspend fun deleteAllBrowserGroups() = withContext(Dispatchers.IO) {
        browserGroupDao.deleteAllBrowserGroups()
    }

    // Email relationship operations
    fun getBrowserGroupsForEmail(emailId: String): LiveData<List<BrowserGroup>> = 
        browserGroupDao.getBrowserGroupsForEmail(emailId)

    suspend fun removeAllBrowserGroupsFromEmail(emailId: String) = withContext(Dispatchers.IO) {
        browserGroupDao.removeAllBrowserGroupsFromEmail(emailId)
    }
}
package com.emailmanager.repository

import androidx.lifecycle.LiveData
import com.emailmanager.data.dao.EmailDao
import com.emailmanager.data.dao.TagDao
import com.emailmanager.data.dao.BrowserGroupDao
import com.emailmanager.data.dao.UseCaseDao
import com.emailmanager.data.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmailRepository(
    private val emailDao: EmailDao,
    private val tagDao: TagDao,
    private val browserGroupDao: BrowserGroupDao,
    private val useCaseDao: UseCaseDao
) {
    // Email operations
    val allEmails: LiveData<List<Email>> = emailDao.getAllEmails()

    suspend fun insertEmail(email: Email) = withContext(Dispatchers.IO) {
        emailDao.insertEmail(email)
    }

    suspend fun updateEmail(email: Email) = withContext(Dispatchers.IO) {
        emailDao.updateEmail(email)
    }

    suspend fun deleteEmail(email: Email) = withContext(Dispatchers.IO) {
        emailDao.deleteEmail(email)
    }

    fun searchEmails(query: String): LiveData<List<Email>> = emailDao.searchEmails(query)

    fun getEmailById(emailId: String): LiveData<Email> = emailDao.getEmailById(emailId)

    // Tag operations for email
    fun getTagsForEmail(emailId: String): LiveData<List<Tag>> = tagDao.getTagsForEmail(emailId)

    suspend fun addTagToEmail(emailId: String, tagId: Int) = withContext(Dispatchers.IO) {
        tagDao.addEmailTag(EmailTag(emailId, tagId))
    }

    suspend fun removeTagFromEmail(emailId: String, tagId: Int) = withContext(Dispatchers.IO) {
        tagDao.removeEmailTag(EmailTag(emailId, tagId))
    }

    // Browser group operations for email
    fun getBrowserGroupsForEmail(emailId: String): LiveData<List<BrowserGroup>> = 
        browserGroupDao.getBrowserGroupsForEmail(emailId)

    suspend fun addBrowserGroupToEmail(emailId: String, browserGroupId: Int) = withContext(Dispatchers.IO) {
        browserGroupDao.addEmailBrowserGroup(EmailBrowserGroup(emailId, browserGroupId))
    }

    suspend fun removeBrowserGroupFromEmail(emailId: String, browserGroupId: Int) = withContext(Dispatchers.IO) {
        browserGroupDao.removeEmailBrowserGroup(EmailBrowserGroup(emailId, browserGroupId))
    }

    // Use case operations for email
    fun getUseCasesForEmail(emailId: String): LiveData<List<UseCase>> = 
        useCaseDao.getUseCasesForEmail(emailId)

    suspend fun addUseCaseToEmail(emailId: String, useCaseId: Int) = withContext(Dispatchers.IO) {
        useCaseDao.addEmailUseCase(EmailUseCase(emailId, useCaseId))
    }

    suspend fun removeUseCaseFromEmail(emailId: String, useCaseId: Int) = withContext(Dispatchers.IO) {
        useCaseDao.removeEmailUseCase(EmailUseCase(emailId, useCaseId))
    }

    // Batch operations
    suspend fun clearEmailRelations(emailId: String) = withContext(Dispatchers.IO) {
        tagDao.removeAllTagsFromEmail(emailId)
        browserGroupDao.removeAllBrowserGroupsFromEmail(emailId)
        useCaseDao.removeAllUseCasesFromEmail(emailId)
    }
}
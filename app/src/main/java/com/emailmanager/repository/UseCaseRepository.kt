package com.emailmanager.repository

import androidx.lifecycle.LiveData
import com.emailmanager.data.dao.UseCaseDao
import com.emailmanager.data.entity.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UseCaseRepository(private val useCaseDao: UseCaseDao) {
    // Basic use case operations
    val allUseCases: LiveData<List<UseCase>> = useCaseDao.getAllUseCases()

    suspend fun insertUseCase(useCase: UseCase): Long = withContext(Dispatchers.IO) {
        useCaseDao.insertUseCase(useCase)
    }

    suspend fun updateUseCase(useCase: UseCase) = withContext(Dispatchers.IO) {
        useCaseDao.updateUseCase(useCase)
    }

    suspend fun deleteUseCase(useCase: UseCase) = withContext(Dispatchers.IO) {
        useCaseDao.deleteUseCase(useCase)
    }

    fun searchUseCases(query: String): LiveData<List<UseCase>> = 
        useCaseDao.searchUseCases(query)

    fun getUseCaseById(useCaseId: Int): LiveData<UseCase> = 
        useCaseDao.getUseCaseById(useCaseId)

    // Batch operations
    suspend fun deleteAllUseCases() = withContext(Dispatchers.IO) {
        useCaseDao.deleteAllUseCases()
    }

    // Email relationship operations
    fun getUseCasesForEmail(emailId: String): LiveData<List<UseCase>> = 
        useCaseDao.getUseCasesForEmail(emailId)

    suspend fun removeAllUseCasesFromEmail(emailId: String) = withContext(Dispatchers.IO) {
        useCaseDao.removeAllUseCasesFromEmail(emailId)
    }
}
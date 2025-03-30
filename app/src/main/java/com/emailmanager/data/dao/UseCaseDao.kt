package com.emailmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.emailmanager.data.entity.UseCase
import com.emailmanager.data.entity.EmailUseCase

@Dao
interface UseCaseDao {
    @Query("SELECT * FROM usecases ORDER BY usecase_name ASC")
    fun getAllUseCases(): LiveData<List<UseCase>>

    @Query("SELECT * FROM usecases WHERE usecase_id = :usecaseId")
    fun getUseCaseById(usecaseId: Int): LiveData<UseCase>

    @Query("""
        SELECT * FROM usecases 
        WHERE usecase_name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY usecase_name ASC
    """)
    fun searchUseCases(query: String): LiveData<List<UseCase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUseCase(useCase: UseCase): Long

    @Update
    suspend fun updateUseCase(useCase: UseCase)

    @Delete
    suspend fun deleteUseCase(useCase: UseCase)

    @Query("DELETE FROM usecases")
    suspend fun deleteAllUseCases()

    // Email-UseCase relationship operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmailUseCase(emailUseCase: EmailUseCase)

    @Delete
    suspend fun removeEmailUseCase(emailUseCase: EmailUseCase)

    @Query("""
        SELECT * FROM usecases 
        INNER JOIN email_usecases 
        ON usecases.usecase_id = email_usecases.usecase_id 
        WHERE email_usecases.email_id = :emailId
    """)
    fun getUseCasesForEmail(emailId: String): LiveData<List<UseCase>>

    @Query("DELETE FROM email_usecases WHERE email_id = :emailId")
    suspend fun removeAllUseCasesFromEmail(emailId: String)
}
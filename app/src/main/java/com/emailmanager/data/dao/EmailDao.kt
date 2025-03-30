package com.emailmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.emailmanager.data.entity.Email
import java.util.Date

@Dao
interface EmailDao {
    @Query("SELECT * FROM emails ORDER BY created_at DESC")
    fun getAllEmails(): LiveData<List<Email>>

    @Query("SELECT * FROM emails WHERE email_id = :emailId")
    fun getEmailById(emailId: String): LiveData<Email>

    @Query("""
        SELECT * FROM emails 
        WHERE email_id LIKE '%' || :query || '%' 
        OR first_name LIKE '%' || :query || '%' 
        OR last_name LIKE '%' || :query || '%' 
        OR tab_group LIKE '%' || :query || '%'
        ORDER BY created_at DESC
    """)
    fun searchEmails(query: String): LiveData<List<Email>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmail(email: Email)

    @Update
    suspend fun updateEmail(email: Email)

    @Delete
    suspend fun deleteEmail(email: Email)

    @Query("DELETE FROM emails")
    suspend fun deleteAllEmails()

    @Query("UPDATE emails SET updated_at = :timestamp WHERE email_id = :emailId")
    suspend fun updateTimestamp(emailId: String, timestamp: Date = Date())
}
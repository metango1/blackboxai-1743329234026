package com.emailmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.emailmanager.data.entity.BrowserGroup
import com.emailmanager.data.entity.EmailBrowserGroup

@Dao
interface BrowserGroupDao {
    @Query("SELECT * FROM browser_groups ORDER BY browser_group_name ASC")
    fun getAllBrowserGroups(): LiveData<List<BrowserGroup>>

    @Query("SELECT * FROM browser_groups WHERE browser_group_id = :browserGroupId")
    fun getBrowserGroupById(browserGroupId: Int): LiveData<BrowserGroup>

    @Query("""
        SELECT * FROM browser_groups 
        WHERE browser_group_name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY browser_group_name ASC
    """)
    fun searchBrowserGroups(query: String): LiveData<List<BrowserGroup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrowserGroup(browserGroup: BrowserGroup): Long

    @Update
    suspend fun updateBrowserGroup(browserGroup: BrowserGroup)

    @Delete
    suspend fun deleteBrowserGroup(browserGroup: BrowserGroup)

    @Query("DELETE FROM browser_groups")
    suspend fun deleteAllBrowserGroups()

    // Email-BrowserGroup relationship operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmailBrowserGroup(emailBrowserGroup: EmailBrowserGroup)

    @Delete
    suspend fun removeEmailBrowserGroup(emailBrowserGroup: EmailBrowserGroup)

    @Query("""
        SELECT * FROM browser_groups 
        INNER JOIN email_browser_groups 
        ON browser_groups.browser_group_id = email_browser_groups.browser_group_id 
        WHERE email_browser_groups.email_id = :emailId
    """)
    fun getBrowserGroupsForEmail(emailId: String): LiveData<List<BrowserGroup>>

    @Query("DELETE FROM email_browser_groups WHERE email_id = :emailId")
    suspend fun removeAllBrowserGroupsFromEmail(emailId: String)
}
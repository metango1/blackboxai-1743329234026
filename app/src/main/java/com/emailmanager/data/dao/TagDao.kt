package com.emailmanager.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.emailmanager.data.entity.Tag
import com.emailmanager.data.entity.EmailTag

@Dao
interface TagDao {
    @Query("SELECT * FROM tags ORDER BY tag_name ASC")
    fun getAllTags(): LiveData<List<Tag>>

    @Query("SELECT * FROM tags WHERE tag_id = :tagId")
    fun getTagById(tagId: Int): LiveData<Tag>

    @Query("""
        SELECT * FROM tags 
        WHERE tag_name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY tag_name ASC
    """)
    fun searchTags(query: String): LiveData<List<Tag>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag): Long

    @Update
    suspend fun updateTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("DELETE FROM tags")
    suspend fun deleteAllTags()

    // Email-Tag relationship operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmailTag(emailTag: EmailTag)

    @Delete
    suspend fun removeEmailTag(emailTag: EmailTag)

    @Query("SELECT * FROM tags INNER JOIN email_tags ON tags.tag_id = email_tags.tag_id WHERE email_tags.email_id = :emailId")
    fun getTagsForEmail(emailId: String): LiveData<List<Tag>>

    @Query("DELETE FROM email_tags WHERE email_id = :emailId")
    suspend fun removeAllTagsFromEmail(emailId: String)
}
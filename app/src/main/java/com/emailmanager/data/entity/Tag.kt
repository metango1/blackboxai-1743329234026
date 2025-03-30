package com.emailmanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val tagId: Int = 0,
    val tagName: String,
    val description: String?,
    val createdAt: Date = Date()
)
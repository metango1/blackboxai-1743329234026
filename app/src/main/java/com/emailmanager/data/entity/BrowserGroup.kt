package com.emailmanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "browser_groups")
data class BrowserGroup(
    @PrimaryKey(autoGenerate = true)
    val browserGroupId: Int = 0,
    val browserGroupName: String,
    val description: String?,
    val createdAt: Date = Date()
)
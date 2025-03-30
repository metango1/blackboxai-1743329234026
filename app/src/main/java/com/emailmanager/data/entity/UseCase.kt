package com.emailmanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "usecases")
data class UseCase(
    @PrimaryKey(autoGenerate = true)
    val usecaseId: Int = 0,
    val usecaseName: String,
    val description: String?,
    val createdAt: Date = Date()
)
package com.emailmanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "emails")
data class Email(
    @PrimaryKey
    val emailId: String,
    val firstName: String,
    val lastName: String,
    val tabGroup: String,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
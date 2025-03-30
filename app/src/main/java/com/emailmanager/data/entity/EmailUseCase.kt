package com.emailmanager.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "email_usecases",
    primaryKeys = ["emailId", "usecaseId"],
    foreignKeys = [
        ForeignKey(
            entity = Email::class,
            parentColumns = ["emailId"],
            childColumns = ["emailId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UseCase::class,
            parentColumns = ["usecaseId"],
            childColumns = ["usecaseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EmailUseCase(
    val emailId: String,
    val usecaseId: Int,
    val createdAt: Date = Date()
)
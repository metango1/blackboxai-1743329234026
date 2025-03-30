package com.emailmanager.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "email_tags",
    primaryKeys = ["emailId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = Email::class,
            parentColumns = ["emailId"],
            childColumns = ["emailId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["tagId"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EmailTag(
    val emailId: String,
    val tagId: Int,
    val createdAt: Date = Date()
)
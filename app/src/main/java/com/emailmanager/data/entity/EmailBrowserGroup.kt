package com.emailmanager.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "email_browser_groups",
    primaryKeys = ["emailId", "browserGroupId"],
    foreignKeys = [
        ForeignKey(
            entity = Email::class,
            parentColumns = ["emailId"],
            childColumns = ["emailId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BrowserGroup::class,
            parentColumns = ["browserGroupId"],
            childColumns = ["browserGroupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EmailBrowserGroup(
    val emailId: String,
    val browserGroupId: Int,
    val createdAt: Date = Date()
)
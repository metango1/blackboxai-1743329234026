package com.emailmanager.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emailmanager.R
import com.emailmanager.data.entity.BrowserGroup
import com.emailmanager.data.entity.Email
import com.emailmanager.data.entity.Tag
import com.emailmanager.data.entity.UseCase
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.SimpleDateFormat
import java.util.Locale

class EmailAdapter(
    private val onEditClick: (Email) -> Unit
) : ListAdapter<EmailWithRelations, EmailAdapter.EmailViewHolder>(EmailDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_email, parent, false)
        return EmailViewHolder(view, onEditClick)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EmailViewHolder(
        itemView: View,
        private val onEditClick: (Email) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val emailIdText: TextView = itemView.findViewById(R.id.email_id_text)
        private val nameText: TextView = itemView.findViewById(R.id.name_text)
        private val tabGroupText: TextView = itemView.findViewById(R.id.tab_group_text)
        private val createdAtText: TextView = itemView.findViewById(R.id.created_at_text)
        private val editButton: MaterialButton = itemView.findViewById(R.id.edit_button)
        private val tagChipGroup: ChipGroup = itemView.findViewById(R.id.tag_chip_group)
        private val browserGroupChipGroup: ChipGroup = itemView.findViewById(R.id.browser_group_chip_group)
        private val useCaseChipGroup: ChipGroup = itemView.findViewById(R.id.use_case_chip_group)

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        fun bind(emailWithRelations: EmailWithRelations) {
            val email = emailWithRelations.email
            
            emailIdText.text = email.emailId
            nameText.text = "${email.firstName} ${email.lastName}"
            tabGroupText.text = email.tabGroup
            createdAtText.text = "Created: ${dateFormat.format(email.createdAt)}"

            editButton.setOnClickListener { onEditClick(email) }

            // Set up tags
            tagChipGroup.removeAllViews()
            emailWithRelations.tags.forEach { tag ->
                tagChipGroup.addView(createChip(tag.tagName))
            }

            // Set up browser groups
            browserGroupChipGroup.removeAllViews()
            emailWithRelations.browserGroups.forEach { group ->
                browserGroupChipGroup.addView(createChip(group.browserGroupName))
            }

            // Set up use cases
            useCaseChipGroup.removeAllViews()
            emailWithRelations.useCases.forEach { useCase ->
                useCaseChipGroup.addView(createChip(useCase.usecaseName))
            }
        }

        private fun createChip(text: String): Chip {
            return Chip(itemView.context).apply {
                this.text = text
                isClickable = false
                setChipBackgroundColorResource(R.color.chip_background_color)
            }
        }
    }

    data class EmailWithRelations(
        val email: Email,
        val tags: List<Tag> = emptyList(),
        val browserGroups: List<BrowserGroup> = emptyList(),
        val useCases: List<UseCase> = emptyList()
    )

    class EmailDiffCallback : DiffUtil.ItemCallback<EmailWithRelations>() {
        override fun areItemsTheSame(oldItem: EmailWithRelations, newItem: EmailWithRelations): Boolean {
            return oldItem.email.emailId == newItem.email.emailId
        }

        override fun areContentsTheSame(oldItem: EmailWithRelations, newItem: EmailWithRelations): Boolean {
            return oldItem == newItem
        }
    }
}
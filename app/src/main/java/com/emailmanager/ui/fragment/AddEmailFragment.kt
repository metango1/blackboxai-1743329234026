package com.emailmanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.emailmanager.R
import com.emailmanager.data.entity.BrowserGroup
import com.emailmanager.data.entity.Tag
import com.emailmanager.data.entity.UseCase
import com.emailmanager.viewmodel.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddEmailFragment : Fragment() {

    private val emailViewModel: EmailViewModel by viewModels()
    private val tagViewModel: TagViewModel by viewModels()
    private val browserGroupViewModel: BrowserGroupViewModel by viewModels()
    private val useCaseViewModel: UseCaseViewModel by viewModels()

    private lateinit var emailIdInput: TextInputEditText
    private lateinit var firstNameInput: TextInputEditText
    private lateinit var lastNameInput: TextInputEditText
    private lateinit var tabGroupInput: AutoCompleteTextView
    private lateinit var tagChipGroup: ChipGroup
    private lateinit var browserGroupChipGroup: ChipGroup
    private lateinit var useCaseChipGroup: ChipGroup
    private lateinit var saveButton: ExtendedFloatingActionButton

    private val selectedTags = mutableSetOf<Int>()
    private val selectedBrowserGroups = mutableSetOf<Int>()
    private val selectedUseCases = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        initializeViews(view)
        setupObservers()
        setupClickListeners()
    }

    private fun initializeViews(view: View) {
        emailIdInput = view.findViewById(R.id.email_id_edit_text)
        firstNameInput = view.findViewById(R.id.first_name_edit_text)
        lastNameInput = view.findViewById(R.id.last_name_edit_text)
        tabGroupInput = view.findViewById(R.id.tab_group_auto_complete)
        tagChipGroup = view.findViewById(R.id.tag_chip_group)
        browserGroupChipGroup = view.findViewById(R.id.browser_group_chip_group)
        useCaseChipGroup = view.findViewById(R.id.use_case_chip_group)
        saveButton = view.findViewById(R.id.save_button)

        // Setup tab group dropdown
        val tabGroups = arrayOf("Personal", "Work", "Social", "Other") // Example groups
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, tabGroups)
        tabGroupInput.setAdapter(adapter)
    }

    private fun setupObservers() {
        // Observe tags
        tagViewModel.allTags.observe(viewLifecycleOwner, Observer { tags ->
            updateTagChips(tags)
        })

        // Observe browser groups
        browserGroupViewModel.allBrowserGroups.observe(viewLifecycleOwner, Observer { groups ->
            updateBrowserGroupChips(groups)
        })

        // Observe use cases
        useCaseViewModel.allUseCases.observe(viewLifecycleOwner, Observer { useCases ->
            updateUseCaseChips(useCases)
        })

        // Observe operation status
        emailViewModel.operationStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                is OperationStatus.Success -> {
                    Snackbar.make(requireView(), status.message, Snackbar.LENGTH_LONG).show()
                    clearForm()
                }
                is OperationStatus.Error -> {
                    Snackbar.make(requireView(), status.message, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupClickListeners() {
        // Add Tag button click
        view?.findViewById<View>(R.id.add_tag_button)?.setOnClickListener {
            showAddTagDialog()
        }

        // Add Browser Group button click
        view?.findViewById<View>(R.id.add_browser_group_button)?.setOnClickListener {
            showAddBrowserGroupDialog()
        }

        // Add Use Case button click
        view?.findViewById<View>(R.id.add_use_case_button)?.setOnClickListener {
            showAddUseCaseDialog()
        }

        // Save button click
        saveButton.setOnClickListener {
            saveEmail()
        }
    }

    private fun updateTagChips(tags: List<Tag>) {
        tagChipGroup.removeAllViews()
        tags.forEach { tag ->
            val chip = createChip(tag.tagName)
            chip.isChecked = selectedTags.contains(tag.tagId)
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedTags.add(tag.tagId)
                else selectedTags.remove(tag.tagId)
            }
            tagChipGroup.addView(chip)
        }
    }

    private fun updateBrowserGroupChips(groups: List<BrowserGroup>) {
        browserGroupChipGroup.removeAllViews()
        groups.forEach { group ->
            val chip = createChip(group.browserGroupName)
            chip.isChecked = selectedBrowserGroups.contains(group.browserGroupId)
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedBrowserGroups.add(group.browserGroupId)
                else selectedBrowserGroups.remove(group.browserGroupId)
            }
            browserGroupChipGroup.addView(chip)
        }
    }

    private fun updateUseCaseChips(useCases: List<UseCase>) {
        useCaseChipGroup.removeAllViews()
        useCases.forEach { useCase ->
            val chip = createChip(useCase.usecaseName)
            chip.isChecked = selectedUseCases.contains(useCase.usecaseId)
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selectedUseCases.add(useCase.usecaseId)
                else selectedUseCases.remove(useCase.usecaseId)
            }
            useCaseChipGroup.addView(chip)
        }
    }

    private fun createChip(text: String): Chip {
        return Chip(requireContext()).apply {
            this.text = text
            isCheckable = true
            setChipBackgroundColorResource(R.color.chip_background_color)
        }
    }

    private fun showAddTagDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_item, null)
        val nameInput = dialogView.findViewById<TextInputLayout>(R.id.name_input_layout)
        val descriptionInput = dialogView.findViewById<TextInputLayout>(R.id.description_input_layout)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add New Tag")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.editText?.text.toString()
                val description = descriptionInput.editText?.text.toString()
                if (name.isNotBlank()) {
                    tagViewModel.insertTag(name, description)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAddBrowserGroupDialog() {
        // Similar to showAddTagDialog but for browser groups
        // Implementation follows the same pattern
    }

    private fun showAddUseCaseDialog() {
        // Similar to showAddTagDialog but for use cases
        // Implementation follows the same pattern
    }

    private fun saveEmail() {
        val emailId = emailIdInput.text.toString()
        val firstName = firstNameInput.text.toString()
        val lastName = lastNameInput.text.toString()
        val tabGroup = tabGroupInput.text.toString()

        if (validateInput(emailId, firstName, lastName, tabGroup)) {
            emailViewModel.insertEmail(emailId, firstName, lastName, tabGroup)
            
            // Add relationships
            selectedTags.forEach { tagId ->
                emailViewModel.addTagToEmail(emailId, tagId)
            }
            selectedBrowserGroups.forEach { groupId ->
                emailViewModel.addBrowserGroupToEmail(emailId, groupId)
            }
            selectedUseCases.forEach { useCaseId ->
                emailViewModel.addUseCaseToEmail(emailId, useCaseId)
            }
        }
    }

    private fun validateInput(
        emailId: String,
        firstName: String,
        lastName: String,
        tabGroup: String
    ): Boolean {
        var isValid = true

        if (emailId.isBlank()) {
            emailIdInput.error = "Email ID is required"
            isValid = false
        }
        if (firstName.isBlank()) {
            firstNameInput.error = "First name is required"
            isValid = false
        }
        if (lastName.isBlank()) {
            lastNameInput.error = "Last name is required"
            isValid = false
        }
        if (tabGroup.isBlank()) {
            tabGroupInput.error = "Tab group is required"
            isValid = false
        }

        return isValid
    }

    private fun clearForm() {
        emailIdInput.text?.clear()
        firstNameInput.text?.clear()
        lastNameInput.text?.clear()
        tabGroupInput.text?.clear()
        selectedTags.clear()
        selectedBrowserGroups.clear()
        selectedUseCases.clear()
        
        // Reset chips
        tagChipGroup.clearCheck()
        browserGroupChipGroup.clearCheck()
        useCaseChipGroup.clearCheck()
    }
}
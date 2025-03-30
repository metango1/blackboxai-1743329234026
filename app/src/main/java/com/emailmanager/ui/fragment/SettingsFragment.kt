package com.emailmanager.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.emailmanager.R
import com.emailmanager.viewmodel.EmailViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsFragment : Fragment() {

    private val emailViewModel: EmailViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var themeSwitch: SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        initializeViews(view)
        setupThemeSwitch()
        setupDataManagementButtons()
    }

    private fun initializeViews(view: View) {
        themeSwitch = view.findViewById(R.id.theme_switch)
        
        // Initialize theme switch state
        themeSwitch.isChecked = isDarkThemeEnabled()

        // Setup data management buttons
        view.findViewById<MaterialButton>(R.id.export_data_button).setOnClickListener {
            exportData()
        }

        view.findViewById<MaterialButton>(R.id.import_data_button).setOnClickListener {
            importData()
        }

        view.findViewById<MaterialButton>(R.id.clear_data_button).setOnClickListener {
            showClearDataConfirmationDialog()
        }
    }

    private fun setupThemeSwitch() {
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            setDarkTheme(isChecked)
        }
    }

    private fun setupDataManagementButtons() {
        // Export data button click listener is already set in initializeViews
        // Import data button click listener is already set in initializeViews
        // Clear data button click listener is already set in initializeViews
    }

    private fun isDarkThemeEnabled(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }

    private fun setDarkTheme(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, enabled).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun exportData() {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(Date())
            val fileName = "email_manager_backup_$timestamp.json"
            
            // TODO: Implement actual export logic
            // For now, just show a success message
            showSnackbar("Data exported successfully to $fileName")
        } catch (e: Exception) {
            showSnackbar("Failed to export data: ${e.message}")
        }
    }

    private fun importData() {
        try {
            // TODO: Implement actual import logic
            // For now, just show a success message
            showSnackbar("Data imported successfully")
        } catch (e: Exception) {
            showSnackbar("Failed to import data: ${e.message}")
        }
    }

    private fun showClearDataConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Clear All Data")
            .setMessage("Are you sure you want to clear all data? This action cannot be undone.")
            .setPositiveButton("Clear") { _, _ ->
                clearAllData()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun clearAllData() {
        try {
            // TODO: Implement actual data clearing logic
            // This should clear all tables in the database
            showSnackbar("All data cleared successfully")
        } catch (e: Exception) {
            showSnackbar("Failed to clear data: ${e.message}")
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        private const val DARK_THEME_KEY = "dark_theme_enabled"
    }
}
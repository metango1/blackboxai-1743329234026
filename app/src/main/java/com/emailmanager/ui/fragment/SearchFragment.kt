package com.emailmanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emailmanager.R
import com.emailmanager.data.entity.Email
import com.emailmanager.ui.adapter.EmailAdapter
import com.emailmanager.viewmodel.EmailViewModel
import com.emailmanager.viewmodel.TagViewModel
import com.emailmanager.viewmodel.BrowserGroupViewModel
import com.emailmanager.viewmodel.UseCaseViewModel
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val emailViewModel: EmailViewModel by viewModels()
    private val tagViewModel: TagViewModel by viewModels()
    private val browserGroupViewModel: BrowserGroupViewModel by viewModels()
    private val useCaseViewModel: UseCaseViewModel by viewModels()

    private lateinit var searchInput: TextInputEditText
    private lateinit var filterChipGroup: ChipGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var emailAdapter: EmailAdapter

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupRecyclerView()
        setupSearchInput()
        setupFilterChips()
        observeData()
    }

    private fun initializeViews(view: View) {
        searchInput = view.findViewById(R.id.search_input)
        filterChipGroup = view.findViewById(R.id.filter_chip_group)
        recyclerView = view.findViewById(R.id.search_results_recycler_view)
        emptyView = view.findViewById(R.id.empty_view)
    }

    private fun setupRecyclerView() {
        emailAdapter = EmailAdapter { email ->
            // Navigate to edit screen with email ID
            findNavController().navigate(
                SearchFragmentDirections.actionSearchToAddEmail()
                    .setEmailId(email.emailId)
            )
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = emailAdapter
        }
    }

    private fun setupSearchInput() {
        searchInput.addTextChangedListener { text ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(300) // Debounce search input
                performSearch(text.toString())
            }
        }
    }

    private fun setupFilterChips() {
        filterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(300)
                performSearch(searchInput.text.toString(), getSearchFilter(checkedId))
            }
        }
    }

    private fun getSearchFilter(chipId: Int): SearchFilter {
        return when (chipId) {
            R.id.filter_email -> SearchFilter.EMAIL
            R.id.filter_name -> SearchFilter.NAME
            R.id.filter_tag -> SearchFilter.TAG
            R.id.filter_browser_group -> SearchFilter.BROWSER_GROUP
            R.id.filter_use_case -> SearchFilter.USE_CASE
            else -> SearchFilter.ALL
        }
    }

    private fun performSearch(query: String, filter: SearchFilter = SearchFilter.ALL) {
        when (filter) {
            SearchFilter.ALL -> emailViewModel.searchEmails(query)
            SearchFilter.EMAIL -> emailViewModel.searchEmails(query) // You might want to add specific search methods
            SearchFilter.NAME -> emailViewModel.searchEmails(query)
            SearchFilter.TAG -> tagViewModel.searchTags(query)
            SearchFilter.BROWSER_GROUP -> browserGroupViewModel.searchBrowserGroups(query)
            SearchFilter.USE_CASE -> useCaseViewModel.searchUseCases(query)
        }.observe(viewLifecycleOwner) { results ->
            updateSearchResults(results)
        }
    }

    private fun updateSearchResults(results: List<Any>) {
        when {
            results.isEmpty() -> {
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            }
            else -> {
                recyclerView.visibility = View.VISIBLE
                emptyView.visibility = View.GONE

                // Convert results to EmailWithRelations
                val emailResults = when (results.firstOrNull()) {
                    is Email -> results.map { email ->
                        EmailAdapter.EmailWithRelations(
                            email = email as Email,
                            tags = tagViewModel.getTagsForEmail((email as Email).emailId).value ?: emptyList(),
                            browserGroups = browserGroupViewModel.getBrowserGroupsForEmail((email as Email).emailId).value ?: emptyList(),
                            useCases = useCaseViewModel.getUseCasesForEmail((email as Email).emailId).value ?: emptyList()
                        )
                    }
                    else -> emptyList()
                }
                emailAdapter.submitList(emailResults)
            }
        }
    }

    private fun observeData() {
        emailViewModel.operationStatus.observe(viewLifecycleOwner) { status ->
            // Handle operation status (e.g., show error messages)
        }
    }

    enum class SearchFilter {
        ALL, EMAIL, NAME, TAG, BROWSER_GROUP, USE_CASE
    }
}
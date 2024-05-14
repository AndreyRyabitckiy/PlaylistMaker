package com.example.playlistmaker.search.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.models.ResponseStatus
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.presentation.view_model.SearchFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchFragmentViewModel by viewModel()
    val adapter by lazy { TrackAdapter() }
    private var debounceClick = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.tracks.observe(viewLifecycleOwner) {
            adapter.data = it
        }
        viewModel.isClickAllowed.observe(viewLifecycleOwner){
            debounceClick = it
        }

        viewModel.searchStatus.observe(viewLifecycleOwner) { tracksListChanged(it) }
        viewModel.showHistory.observe(viewLifecycleOwner) { answer->
                binding.bClearHistorySearch.isVisible = answer
                binding.tvHistorySearch.isVisible = answer
        }

        adapter.onClick = { item -> onClickAdapter(item) }

        binding.run {
            rwTrack.adapter = adapter

            bClearHistorySearch.setOnClickListener {
                clearHistory()
            }

            etSearchText.setOnFocusChangeListener { _, hasFocus -> focusListenerUse(hasFocus) }

            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    etTextChangedWatcher(s)
                }
                override fun afterTextChanged(s: Editable?) {
                    //empty
                }
            }

            etSearchText.addTextChangedListener(simpleTextWatcher)
            ivClearIcon.setOnClickListener { clearTextButtonUse() }
            btReserch.setOnClickListener { sendToServer() }
            etSearchText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendToServer()
                }
                false
            }
        }
    }

    private fun onClickAdapter(track: Track) {
        if (debounceClick) {
        viewModel.writeHistory(track)
        viewModel.clickDebounce()
        findNavController().navigate(
            R.id.action_searchFragment_to_musicPlayerFragment,
            bundleOf(TRACK_KEY to track)
        )
        }
    }


    private fun tracksListChanged(searchStatus: ResponseStatus) {
        binding.run {
            when (searchStatus) {
                ResponseStatus.SUCCESS -> {
                    progressBar.isVisible = false
                    rwTrack.isVisible = true
                }

                ResponseStatus.EMPTY -> {
                    progressBar.isVisible = false
                    showHolder(true)
                    ivSunOrWiFi.setImageResource(R.drawable.sun_ic)
                    tvTextHolder.setText(R.string.nothing)
                    btReserch.isVisible = false
                }

                ResponseStatus.LOADING -> {
                    progressBar.isVisible = true
                }

                ResponseStatus.ERROR -> {
                    progressBar.isVisible = false
                    showHolder(true)
                    ivSunOrWiFi.setImageResource(R.drawable.nointernet_ic)
                    tvTextHolder.setText(R.string.Wrong)
                    btReserch.isVisible = true
                }
            }
        }
    }

    private fun clearHistory() {
        viewModel.clearHistory()
        viewModel.showHistoryBoolean(false)
    }

    private fun focusListenerUse(hasFocus: Boolean) {
        binding.run {
            if (hasFocus && etSearchText.text.isEmpty()) {
                showHolder(false)
                viewModel.showHistoryBoolean(true)
            } else {
                viewModel.showHistoryBoolean(false, rewrite = false)
            }
        }
    }

    private fun etTextChangedWatcher(s: CharSequence?) {
        viewModel
        binding.run {
            ivClearIcon.isVisible = !s.isNullOrEmpty()
            s?.let {
                viewModel.setSearchText(it.toString())
            }
            if (etSearchText.hasFocus() && s?.isEmpty() == true) {
                viewModel.showHistoryBoolean(true)
                bClearHistorySearch.isVisible = true
                showHolder(false)
            } else if (etSearchText.hasFocus()) {
                viewModel.searchDebounce()
                viewModel.showHistoryBoolean(false)
                showHolder(false)
            }
        }
    }

    private fun showHolder(state: Boolean) = with(binding) {
        llHolderNothingOrWrong.isVisible = state
        rwTrack.isVisible = !state
    }

    private fun clearTextButtonUse() {
        binding.etSearchText.setText("")
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etSearchText.windowToken, 0)
        viewModel.showHistoryBoolean(true)
        binding.tvHistorySearch.isVisible = true
        binding.bClearHistorySearch.isVisible = true
    }

    private fun sendToServer() = binding.run {
        if (etSearchText.text.isNotBlank()) {
            viewModel.searchTracks(etSearchText.text.toString())
            showHolder(false)
            btReserch.isVisible = false
            progressBar.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume()
    }

    companion object {
        private const val TRACK_KEY = "TRACK"
    }
}
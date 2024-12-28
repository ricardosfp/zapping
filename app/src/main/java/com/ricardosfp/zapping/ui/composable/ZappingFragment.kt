package com.ricardosfp.zapping.ui.composable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ricardosfp.zapping.R
import com.ricardosfp.zapping.ui.viewmodel.zapping.ZappingViewModel
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiDataReady
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiError
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiIdle
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiLoading
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ZappingFragment: Fragment() {
    private lateinit var viewModel: ZappingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this)[ZappingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val initialState = viewModel.uiStateLiveData.value

        return ComposeView(requireContext()).apply {
            setContent {

                if (initialState == null) {
                    // show some error screen
                    ErrorWidget()
                } else {
                    val uiState = viewModel.uiStateLiveData.observeAsState(initialState)

                    StateToScreen(uiState.value)
                }
            }
        }
    }

    @Composable
    private fun StateToScreen(uiState: UiState) {
        when (uiState) {
            UiIdle, UiLoading -> {
                LoadingWidget()
            }

            is UiDataReady -> {
                DataReadyWidget(uiState.dayMap)
            }

            UiError -> {
                ErrorWidget()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo this means that data is not fetched again when recovering from process death.
        //  Implement a cache mechanism. See (refresh = false)
        if (savedInstanceState == null) {
            viewModel.getMatches()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update -> // go get the RSS feed
                viewModel.getMatches()

            else -> return false
        }
        return true
    }

}
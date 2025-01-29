package com.ricardosfp.zapping.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ricardosfp.zapping.R
import com.ricardosfp.zapping.ui.composable.DataReadyWidget
import com.ricardosfp.zapping.ui.composable.ErrorWidget
import com.ricardosfp.zapping.ui.composable.LoadingWidget
import com.ricardosfp.zapping.ui.viewmodel.zapping.ZappingViewModel
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiDataReady
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiError
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiIdle
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiLoading
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<ZappingViewModel>()

        val initialState = viewModel.uiStateLiveData.value

        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = colorResource(R.color.colorPrimary),
                            titleContentColor = Color.White,
                            actionIconContentColor = Color.White),
                        expandedHeight = 50.dp,
                        title = {
                            Text(
                                stringResource(R.string.app_name),
                                fontWeight = FontWeight(500),
                                fontSize = 20.sp)
                        },
                        actions = {
                            IconButton(onClick = { viewModel.getMatches() }) {
                                Icon(
                                    imageVector = Icons.Rounded.Refresh,
                                    null)
                            }
                        }
                    )
                }
            ) { padding ->
                Surface(
                    Modifier
                            .fillMaxSize()
                            .padding(padding)) {
                    if (initialState == null) {
                        // show some error screen
                        ErrorWidget()
                    } else {
                        val uiState = viewModel.uiStateLiveData.observeAsState(initialState)

                        StateToScreen(uiState.value, viewModel::getFormattedDateString)
                    }
                }
            }
        }

        // todo this means that data is not fetched again when recovering from process death.
        //  Implement a cache mechanism. See (refresh = false)
        if (savedInstanceState == null) {
            viewModel.getMatches()
        }
    }
}

@Composable
private fun StateToScreen(uiState: UiState, getFormattedDate: (LocalDate) -> String) {
    when (uiState) {
        UiIdle, UiLoading -> {
            LoadingWidget()
        }

        is UiDataReady -> {
            DataReadyWidget(uiState.dayMap, getFormattedDate)
        }

        UiError -> {
            ErrorWidget()
        }
    }
}
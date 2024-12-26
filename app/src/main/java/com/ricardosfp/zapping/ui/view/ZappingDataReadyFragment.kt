package com.ricardosfp.zapping.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.ricardosfp.zapping.R
import com.ricardosfp.zapping.domain.model.Match
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ZappingDataReadyFragment: Fragment() {

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH)
        const val DAY_MAP_KEY = "DAY_MAP_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val dayMap = (arguments?.getSerializable(DAY_MAP_KEY) as? Map<Date, List<Match>>)
            ?: return ComposeView(requireContext()).apply {
                setContent {
                    Surface(color = Color.White) {
                        ErrorText()
                    }
                }
            }

        // todo what if the list is empty?
        val mapEntryList = dayMap.entries.toList()

        return ComposeView(requireContext()).apply {
            setContent {
                Surface(
                    color = Color.White) {
                    Column {
                        var selectedTab by remember { mutableIntStateOf(0) }

                        ScrollableTabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = Color.White,
                            edgePadding = 20.dp,
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = colorResource(R.color.colorPrimary)
                                )
                            }) {
                            mapEntryList.forEachIndexed { index, mapEntry ->
                                val isSelected = index == selectedTab

                                Tab(
                                    selected = isSelected,
                                    onClick = {
                                        selectedTab = index
                                    },
                                    text = {
                                        Text(DATE_FORMAT.format(mapEntry.key))
                                    },
                                    selectedContentColor = Color.Black,
                                    unselectedContentColor = Color.Gray)
                            }
                        }

                        ZappingDay(mapEntryList[selectedTab].value)
                    }
                }
            }
        }
    }
}
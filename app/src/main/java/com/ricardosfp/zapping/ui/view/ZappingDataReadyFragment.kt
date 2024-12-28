package com.ricardosfp.zapping.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.ricardosfp.zapping.R
import com.ricardosfp.zapping.domain.model.Match
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.DateWithFormattedString
import kotlinx.coroutines.launch

class ZappingDataReadyFragment: Fragment() {

    companion object {
        const val DAY_MAP_KEY = "DAY_MAP_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val dayMap = (arguments?.getSerializable(DAY_MAP_KEY) as? Map<DateWithFormattedString, List<Match>>)
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
                        val pagerState = rememberPagerState(
                            initialPage = 0,
                            pageCount = { mapEntryList.size })

                        TabLayout(mapEntryList, pagerState)
                        HorizontalPager(state = pagerState) { index ->
                            ZappingDay(mapEntryList[index].value)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabLayout(
    dayList: List<Map.Entry<DateWithFormattedString, List<Match>>>,
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = Color.White,
        edgePadding = 20.dp,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = colorResource(R.color.colorPrimary))
        }) {
        dayList.forEachIndexed { index, mapEntry ->
            Tab(
                selected = index == pagerState.currentPage,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(mapEntry.key.formattedDate)
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray)
        }
    }
}
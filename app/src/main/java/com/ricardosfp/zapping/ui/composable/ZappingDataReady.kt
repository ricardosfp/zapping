package com.ricardosfp.zapping.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ricardosfp.zapping.R
import com.ricardosfp.zapping.domain.model.Match
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.DateWithFormattedString
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun DataReadyWidget(dayMap: Map<DateWithFormattedString, List<Match>>) {
    Surface(
        Modifier.fillMaxSize(),
        color = Color.White) {
        Column {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { dayMap.size })

            val mapEntryList by remember { mutableStateOf(dayMap.entries.toList()) }

            TabLayout(mapEntryList, pagerState)
            HorizontalPager(state = pagerState) { index ->
                ZappingDay(mapEntryList[index].value)
            }
        }
    }
}

@Preview
@Composable
private fun DataReadyPreview() {
    val dayMap = mapOf(
        DateWithFormattedString(
            LocalDate.of(
                2024,
                12,
                28),
            "sexta, 28 de dezembro") to listOf(
            Match(
                homeTeam = "Porto",
                awayTeam = "Boavista",
                date = LocalDateTime.of(
                    2024,
                    12,
                    28,
                    20,
                    0),
                channel = "RTP",
                originalText = "")))

    Surface(
        color = Color.White) {
        Column {
            val pagerState = rememberPagerState(
                initialPage = 0,
                pageCount = { dayMap.size })

            val mapEntryList by remember { mutableStateOf(dayMap.entries.toList()) }

            TabLayout(mapEntryList, pagerState)
            HorizontalPager(state = pagerState) { index ->
                ZappingDay(mapEntryList[index].value)
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
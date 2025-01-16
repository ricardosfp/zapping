package com.ricardosfp.zapping.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ricardosfp.zapping.domain.match.model.Match
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val matchListSample = listOf(
    Match(
        homeTeam = "Valência",
        awayTeam = "Porto",
        date = LocalDateTime.of(2024, 10, 10, 10, 10),
        channel = "Sport tv",
        originalText = ""),
    Match(
        homeTeam = "Estrela da Amadora",
        awayTeam = "Beira-Mar",
        date = LocalDateTime.of(2024, 10, 10, 15, 0),
        channel = "Sport tv",
        originalText = "")
)

@Composable
fun ZappingDay(matches: List<Match>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White) {
        LazyColumn {
            items(matches) {
                Surface(
                    Modifier.padding(10.dp, 10.dp)) {
                    MatchLayout(it)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ZappingDayPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White) {
        LazyColumn {
            items(matchListSample) {
                Surface(
                    Modifier.padding(10.dp, 10.dp)) {
                    MatchLayout(it)
                }
            }
        }
    }
}

private val DATE_FORMAT = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
private val TEXT_STYLE = TextStyle(fontSize = 16.sp, color = Color.Black)

@Composable
private fun MatchLayout(match: Match) {
    Column(
        Modifier
                .background(Color.White)) {
        Text("${match.homeTeam} x ${match.awayTeam}", style = TEXT_STYLE)
        Text(DATE_FORMAT.format(match.date), style = TEXT_STYLE)
        Text(match.channel, style = TEXT_STYLE)
    }
}

@Preview
@Composable
private fun MatchLayoutPreview() {
    Column(
        Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(10.dp, 10.dp)) {
        Text("Valência x Porto", style = TEXT_STYLE)
        Text("20:45", style = TEXT_STYLE)
        Text("Sport Tv", style = TEXT_STYLE)
    }
}
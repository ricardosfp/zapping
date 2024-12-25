package com.ricardosfp.zapping.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.ricardosfp.zapping.domain.model.Match
import java.text.SimpleDateFormat
import java.util.Locale

class ZappingDayFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // how to solve this Unchecked Cast ?
        val matches = arguments?.getSerializable(MATCHES_KEY) as? List<Match>

        return ComposeView(requireContext()).apply {
            setContent {
                Surface(
                    color = Color.White) {
                    if (matches != null) {
                        LazyColumn {
                            items(matches) {
                                Surface(
                                    Modifier.padding(10.dp, 10.dp)) {
                                    MatchLayout(it)
                                }
                            }
                        }
                    } else {
                        ErrorText()
                    }
                }
            }
        }
    }

    companion object {
        private const val MATCHES_KEY = "matches"

        fun newInstance(matches: List<Match>): ZappingDayFragment {
            val fragment = ZappingDayFragment()
            val bundle = Bundle()
            bundle.putSerializable(MATCHES_KEY, ArrayList(matches))
            fragment.arguments = bundle
            return fragment
        }
    }
}

private val DATE_FORMAT = SimpleDateFormat("HH:mm", Locale.ENGLISH)
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
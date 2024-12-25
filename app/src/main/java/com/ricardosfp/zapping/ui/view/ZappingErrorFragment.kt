package com.ricardosfp.zapping.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import com.ricardosfp.zapping.R

class ZappingErrorFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Surface(
                    color = Color.White) {
                    ErrorText()
                }
            }
        }
    }
}

private val TEXT_STYLE = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
    color = Color.Black)

@Composable
fun ErrorText() {
    ConstraintLayout {
        val text = createRef()

        Text(
            stringResource(R.string.error_loading_data),
            modifier = Modifier.constrainAs(text) {
                centerTo(parent)
                width = Dimension.percent(0.8F)
            }, style = TEXT_STYLE
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun ErrorTextPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout {
            val text = createRef()

            Text(
                stringResource(R.string.error_loading_data),
                modifier = Modifier.constrainAs(text) {
                    centerTo(parent)
                    width = Dimension.percent(0.8F)
                }, style = TEXT_STYLE
            )
        }
    }
}
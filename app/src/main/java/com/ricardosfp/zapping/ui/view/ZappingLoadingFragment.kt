package com.ricardosfp.zapping.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import com.ricardosfp.zapping.R

class ZappingLoadingFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Surface(
                    color = Color.White) {
                    IndeterminateCircularProgressIndicator()
                }
            }
        }
    }

}

@Composable
private fun IndeterminateCircularProgressIndicator() {
    ConstraintLayout {
        val circle = createRef()

        CircularProgressIndicator(
            Modifier
                    .constrainAs(circle) {
                        centerTo(parent)
                    }
                    .size(160.dp), color = colorResource(R.color.colorPrimary), strokeWidth = 8.dp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun IndeterminateCircularProgressIndicatorPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout {
            val circle = createRef()

            CircularProgressIndicator(
                Modifier
                        .constrainAs(circle) {
                            centerTo(parent)
                        }
                        .size(160.dp),
                color = colorResource(R.color.colorPrimary), strokeWidth = 8.dp
            )
        }
    }
}
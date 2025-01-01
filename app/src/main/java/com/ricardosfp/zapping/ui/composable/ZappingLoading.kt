package com.ricardosfp.zapping.ui.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ricardosfp.zapping.R

@Composable
fun LoadingWidget() {
    Surface(
        Modifier.fillMaxSize(),
        color = Color.White) {
        IndeterminateCircularProgressIndicator()
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
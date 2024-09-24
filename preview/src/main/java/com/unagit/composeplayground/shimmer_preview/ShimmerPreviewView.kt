package com.unagit.composeplayground.shimmer_preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unagit.composeplayground.common.anim.shimmerBrush

@Composable
fun ShimmerPreviewView() {

    val shimmerBrush = shimmerBrush(
        brushColor = Color.Yellow
    )

    val shimmerBrush2 = shimmerBrush(
        brushColor = Color.Yellow,
        customColors = listOf(
            Color.LightGray,
            Color.LightGray,
            Color.Yellow,
            Color.LightGray,
            Color.LightGray,
        )
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.LightGray)
            .background(brush = shimmerBrush)
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = "Some Text",
        )

        Text(
            text = "Some Text",
            style = TextStyle(
                brush = shimmerBrush2,
                fontSize = 24.sp,
            ),
        )
    }
}

@Composable
@Preview
private fun ShimmerPreviewViewPreview() {
    ShimmerPreviewView()
}

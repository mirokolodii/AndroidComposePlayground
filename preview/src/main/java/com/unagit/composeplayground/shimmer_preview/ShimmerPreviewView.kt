package com.unagit.composeplayground.shimmer_preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unagit.composeplayground.common.anim.shimmerBrush
import com.unagit.composeplayground.preview.R

@Composable
fun ShimmerPreviewView() {

    val shimmerBrush = shimmerBrush(
        brushColor = Color.Yellow
    )

    val shimmerBrush2 = shimmerBrush(
        customColors = listOf(
            Color.LightGray,
            Color.LightGray,
            Color.Yellow,
            Color.LightGray,
            Color.LightGray,
        )
    )

    val imageBrush = ShaderBrush(
        ImageShader(
            ImageBitmap.imageResource(id = R.drawable.jetpack_compose_logo)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
//                brush = shimmerBrush2,
                fontSize = 24.sp,
            ),
        )

        Canvas(
            modifier = Modifier
                .background(color = Color(0xFFdb9456))
                .size(200.dp),
            onDraw = {
                drawCircle(
                    imageBrush,
                )
                drawCircle(shimmerBrush)
            },

            )

    }
}

@Composable
@Preview
private fun ShimmerPreviewViewPreview() {
    ShimmerPreviewView()
}

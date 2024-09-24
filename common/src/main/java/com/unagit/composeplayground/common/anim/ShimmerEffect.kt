package com.unagit.composeplayground.common.anim

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun shimmerBrush(
    brushColor: Color,
    customColors: List<Color> = emptyList(),
    iterations: Int = 0,
    widthOfShadowBrushDp: Dp = 300.dp,
    endOfOffsetYDp: Dp = 100.dp,
    durationMillis: Int = 2000,
    delay: Long = 0,
): Brush {
    require(iterations >= 0)

    val infinite = iterations == 0

    val colors = customColors.takeIf { it.isNotEmpty() } ?: listOf(
        Color.Transparent,
        Color.Transparent,
        brushColor.copy(alpha = 0.5f),
        brushColor,
        brushColor.copy(alpha = 0.5f),
        Color.Transparent,
        Color.Transparent,
    )

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val width: Float = with(density) { configuration.screenWidthDp.dp.toPx() }
    val endOffsetY: Float = with(density) { endOfOffsetYDp.toPx() }
    val widthOfShadowBrush: Float = with(density) { widthOfShadowBrushDp.toPx() }

    val targetValue by remember(width) { mutableFloatStateOf(width * 2) }

    val animatedValue: Float = if (infinite) {
        rememberInfiniteAnimatedValue(
            targetValue = targetValue,
            durationMillis = durationMillis,
        )
    } else {
        rememberAnimatedValue(
            targetValue = width * 2,
            iterations = iterations,
            durationMillis = durationMillis,
            delay = delay,
        )
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(x = animatedValue - widthOfShadowBrush, y = -endOffsetY),
        end = Offset(x = animatedValue, y = endOffsetY),
    )
}

@Composable
private fun rememberInfiniteAnimatedValue(
    targetValue: Float,
    durationMillis: Int,
): Float {
    val value: Float by remember(targetValue) { mutableFloatStateOf(targetValue * 2) }

    val transition = rememberInfiniteTransition(label = "infiniteTransition")

    return transition.animateFloat(
        initialValue = 0f,
        targetValue = value,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    ).value
}

@Composable
private fun rememberAnimatedValue(
    targetValue: Float,
    iterations: Int,
    durationMillis: Int,
    delay: Long,
): Float {

    var value: Float by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        delay(delay)
        value = targetValue
    }

    return animateFloatAsState(
        targetValue = value,
        animationSpec = repeatable(
            iterations = iterations,
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
        ),
        label = "Repeatable animation"
    ).value
}

package com.test.snotes.item

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.rememberNavController
import com.test.snotes.utils.LoaderState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GlobalLoaderOverlayPreview() {
    val navController = rememberNavController()
    GlobalLoaderOverlay()
}


@Composable
fun GlobalLoaderOverlay() {
    if (LoaderState.isLoading.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .pointerInput(Unit) {} // Block touches
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            DottedCircularLoader()
        }
    }
}


@Composable
fun DottedCircularLoader(
    modifier: Modifier = Modifier,
    dotCount: Int = 12,
    dotRadius: Dp = 5.dp,
    circleRadius: Dp = 30.dp,
    animationDuration: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = LinearEasing)
        )
    )

    Box(
        modifier = modifier
            .size(circleRadius * 2)
            .graphicsLayer {
                rotationZ = rotation
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = size.center
            val radius = circleRadius.toPx()
            val dotSize = dotRadius.toPx()

            val baseColor = Color(0xFFFF1E00)
            val endColor = Color(0xFFFF9084)

            repeat(dotCount) { i ->
                val angle = (2 * PI / dotCount * i).toFloat()
                val x = center.x + radius * cos(angle)
                val y = center.y + radius * sin(angle)
                val color = lerp(baseColor, endColor, i / dotCount.toFloat())

                drawCircle(
                    color = color,
                    radius = dotSize,
                    center = Offset(x, y)
                )
            }
        }
    }
}


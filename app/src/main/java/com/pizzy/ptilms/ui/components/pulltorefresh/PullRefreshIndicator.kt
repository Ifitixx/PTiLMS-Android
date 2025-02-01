package com.pizzy.ptilms.ui.components.pulltorefresh

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

/**
 * An indicator for pull-to-refresh.
 *
 * @param refreshing Whether the indicator should show the refreshing state.
 * @param state The [PullRefreshState] which controls the state of the pull to refresh.
 * @param modifier The modifier to apply to the indicator.
 * @param colors The colors to use for the indicator.
 * @param scale Whether the indicator should scale with pull progress.
 * @param content The content of the indicator.
 */
@Composable
fun PullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    colors: PullRefreshIndicatorColors = PullRefreshIndicatorDefaults.colors(),
    scale: Boolean = false,
    content: @Composable BoxScope.() -> Unit = {
        Crossfade(
            targetState = refreshing,
            label = "refreshing",
        ) { isRefreshing ->
            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(PullRefreshIndicatorDefaults.IndicatorSize),
                    color = colors.indicatorColor,
                    strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth,
                )
            } else {
                val adjustedPercent = (state.progress).coerceIn(0f, 1f)
                val arrowScale = lerp(
                    PullRefreshIndicatorDefaults.ArrowStartScale,
                    PullRefreshIndicatorDefaults.ArrowEndScale,
                    adjustedPercent,
                )
                val arrowAlpha = lerp(
                    PullRefreshIndicatorDefaults.ArrowStartAlpha,
                    PullRefreshIndicatorDefaults.ArrowEndAlpha,
                    adjustedPercent,
                )
                val arrowRotation = lerp(
                    PullRefreshIndicatorDefaults.ArrowStartRotation,
                    PullRefreshIndicatorDefaults.ArrowEndRotation,
                    adjustedPercent,
                )
                val arrowStrokeWidth = lerp(
                    PullRefreshIndicatorDefaults.ArrowStartStrokeWidth,
                    PullRefreshIndicatorDefaults.ArrowEndStrokeWidth,
                    adjustedPercent,
                )
                val circleRadius = lerp(
                    PullRefreshIndicatorDefaults.CircleStartRadius,
                    PullRefreshIndicatorDefaults.CircleEndRadius,
                    adjustedPercent,
                )
                val circleStrokeWidth = lerp(
                    PullRefreshIndicatorDefaults.CircleStartStrokeWidth,
                    PullRefreshIndicatorDefaults.CircleEndStrokeWidth,
                    adjustedPercent,
                )
                Canvas(
                    modifier = Modifier
                        .size(PullRefreshIndicatorDefaults.IndicatorSize)
                        .drawWithContent {
                            drawContent()
                            drawCircle(
                                color = colors.indicatorColor,
                                radius = circleRadius,
                                style = Stroke(circleStrokeWidth),
                            )
                        },
                ) {
                    rotate(arrowRotation) {
                        drawPath(
                            path = Path().apply {
                                fillType = PathFillType.EvenOdd
                                moveTo(0f, 0f)
                                lineTo(arrowScale * PullRefreshIndicatorDefaults.ArrowWidth, 0f)
                                lineTo(
                                    arrowScale * PullRefreshIndicatorDefaults.ArrowWidth / 2,
                                    arrowScale * PullRefreshIndicatorDefaults.ArrowHeight,
                                )
                                close()
                            },
                            color = colors.indicatorColor,
                            alpha = arrowAlpha,
                        )
                    }
                }
            }
        }
    },
) {
    val indicatorModifier = modifier
        .size(PullRefreshIndicatorDefaults.IndicatorSize)
        .pullRefreshIndicatorTransform(state, scale)
        .shadow(PullRefreshIndicatorDefaults.Elevation)
    Box(
        modifier = indicatorModifier,
        contentAlignment = Alignment.Center,
        content = content,
    )
}

/**
 * Default parameter values for [PullRefreshIndicator].
 */
object PullRefreshIndicatorDefaults {
    /**
     * The default elevation used for the indicator.
     */
    val Elevation = 6.dp

    /**
     * The default colors used for the indicator.
     */
    @Composable
    fun colors(
        indicatorColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    ): PullRefreshIndicatorColors = DefaultPullRefreshIndicatorColors(
        indicatorColor = indicatorColor,
    )

    internal const val ArrowStartScale = 0.5f
    internal const val ArrowEndScale = 1f
    internal const val ArrowStartAlpha = 0f
    internal const val ArrowEndAlpha = 1f
    internal const val ArrowStartRotation = 0f
    internal const val ArrowEndRotation = 180f
    internal val ArrowStartStrokeWidth = 2.dp.value
    internal val ArrowEndStrokeWidth = 4.dp.value
    internal val CircleStartRadius = 10.dp.value
    internal val CircleEndRadius = 16.dp.value
    internal val CircleStartStrokeWidth = 2.dp.value
    internal val CircleEndStrokeWidth = 4.dp.value
    internal val IndicatorSize = 40.dp
    internal val ArrowWidth = 10.dp.value
    internal val ArrowHeight = 5.dp.value
}

/**
 * Represents the colors used by [PullRefreshIndicator].
 */
@Immutable
interface PullRefreshIndicatorColors {
    /**
     * The color used for the indicator.
     */
    val indicatorColor: androidx.compose.ui.graphics.Color
}

/**
 * Default [PullRefreshIndicatorColors] implementation.
 */
private data class DefaultPullRefreshIndicatorColors(
    override val indicatorColor: androidx.compose.ui.graphics.Color,
) : PullRefreshIndicatorColors
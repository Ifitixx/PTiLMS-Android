package com.pizzy.ptilms.ui.components.pulltorefresh

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable

/**
 * A modifier for translating the position and scaling the size of a pull-to-refresh indicator
 * based on the given [PullRefreshState].
 *
 * @param state The [PullRefreshState] which determines the position of the indicator.
 * @param scale A boolean controlling whether the indicator's size scales with pull progress or not.
 */
fun Modifier.pullRefreshIndicatorTransform(
    state: PullRefreshState,
    scale: Boolean = false,
) = inspectable(
    inspectorInfo = debugInspectorInfo {
        name = "pullRefreshIndicatorTransform"
        properties["state"] = state
        properties["scale"] = scale
    },
) {
    Modifier
        .drawWithContent {
            clipRect(
                top = 0f,
                left = -Float.MAX_VALUE,
                right = Float.MAX_VALUE,
                bottom = Float.MAX_VALUE,
            ) {
                this@drawWithContent.drawContent()
            }
        }
        .graphicsLayer {
            translationY = state.position - size.height

            if (scale && !state.refreshing) {
                val scaleFraction = LinearOutSlowInEasing
                    .transform(state.position / state.threshold)
                    .coerceIn(0f, 1f)
                scaleX = scaleFraction
                scaleY = scaleFraction
            }
        }
}
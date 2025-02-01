package com.pizzy.ptilms.ui.components.pulltorefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Velocity

/**
 * A layout that provides the pull-to-refresh gesture.
 *
 * @param state The [PullRefreshState] which controls the state of the pull to refresh.
 * @param enabled Whether the pull-to-refresh gesture is enabled.
 * @param modifier The modifier to apply to the layout.
 * @param contentAlignment The alignment of the content within the layout.
 * @param content The content of the layout.
 */
@Composable
fun PullRefresh(
    state: PullRefreshState,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit,
) {
    val updatedState by rememberUpdatedState(state)
    Box(
        modifier = modifier.pullRefresh(updatedState, enabled),
        contentAlignment = contentAlignment,
        propagateMinConstraints = true,
        content = content,
    )
}

/**
 * A modifier that provides the pull-to-refresh gesture.
 *
 * @param state The [PullRefreshState] which controls the state of the pull to refresh.
 * @param enabled Whether the pull-to-refresh gesture is enabled.
 */
fun Modifier.pullRefresh(
    state: PullRefreshState,
    enabled: Boolean,
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "pullRefresh"
        properties["state"] = state
        properties["enabled"] = enabled
    },
) {
    val updatedState by rememberUpdatedState(state)
    val updatedEnabled by rememberUpdatedState(enabled)
    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }
    val nestedScrollConnection = remember(updatedState, updatedEnabled) {
        PullRefreshNestedScrollConnection(updatedState, updatedEnabled)
    }
    Modifier.nestedScroll(connection = nestedScrollConnection, dispatcher = nestedScrollDispatcher)
}

private class PullRefreshNestedScrollConnection(
    private val state: PullRefreshState,
    private val enabled: Boolean,
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (!enabled) return Offset.Zero
        val delta = available.y
        return if (delta < 0 && source == NestedScrollSource.UserInput) {
            state.onPull(delta).toOffset()
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        if (!enabled) return Offset.Zero
        val delta = available.y
        return if (delta > 0 && source == NestedScrollSource.UserInput) {
            state.onPull(delta).toOffset()
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (!enabled) return Velocity.Zero
        val velocity = available.y
        if (velocity < 0) {
            // We're only interested in downward flings (negative velocity).
            // Consume all of the downward velocity to trigger the release behavior.
            state.onRelease(velocity)
        }
        // We've consumed the relevant velocity (if any), so return Zero to prevent
        // the children from handling it. Upward flings are not consumed here.
        return Velocity.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (!enabled) return Velocity.Zero
        state.onRelease(available.y)
        return Velocity.Zero
    }

    private fun Float.toOffset() = Offset(0f, this)
}
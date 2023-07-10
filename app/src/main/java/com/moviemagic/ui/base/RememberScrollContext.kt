package com.moviemagic.ui.base

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun rememberScrollContext(listState: LazyListState): ScrollContext {
    val scrollContext by remember {
        derivedStateOf {
            ScrollContext(
                isTop = listState.isFirstItemVisible,
                isBottom = listState.isLastItemVisible
            )
        }
    }
    return scrollContext
}

data class ScrollContext(
    val isTop: Boolean, val isBottom: Boolean
)

val LazyListState.isLastItemVisible: Boolean
    get() {
        return if (layoutInfo.totalItemsCount == 0) {
            false
        } else {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
            val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

            return (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount && lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
        }
    }

val LazyListState.isFirstItemVisible: Boolean
    get() = firstVisibleItemIndex == 0
package ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.axxonsoft.utils.ui.TextAutoSize
import utils.Margin
import kotlin.math.absoluteValue

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
    indicatorCount: Int = 7,
    indicatorSize: Dp = Margin.l,
    indicatorShape: Shape = CircleShape,
    horisontalSpacing: Dp = Margin.m,
    scaleDownDistance: Int = 4
) {
    val activeColor = MaterialTheme.colors.primary
    val inactiveColor = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.disabled)
    val activeTextColor = contentColorFor(activeColor)
    val inactiveTextColor = contentColorFor(inactiveColor)

    val lazyListState = rememberLazyListState()

    val totalWidth: Dp = indicatorSize * indicatorCount + horisontalSpacing * (indicatorCount - 1)
    val widthInPx = LocalDensity.current.run { indicatorSize.toPx() }

    LaunchedEffect(currentPage, pageCount) {
        val viewportSize = lazyListState.layoutInfo.viewportSize
        lazyListState.animateScrollToItem(
            currentPage,
            (widthInPx / 2 - viewportSize.width / 2).toInt()
        )
    }

    LazyRow(
        modifier = modifier.width(totalWidth),
        state = lazyListState,
        contentPadding = PaddingValues(vertical = horisontalSpacing),
        horizontalArrangement = Arrangement.spacedBy(
            space = horisontalSpacing,
            alignment = Alignment.CenterHorizontally
        ),
        userScrollEnabled = false
    ) {
        items(pageCount) { index ->
            val distance = (currentPage - index).absoluteValue.toFloat()
            val scale = ((scaleDownDistance - distance) / scaleDownDistance).coerceIn(1f / scaleDownDistance, 1f)
            val scaleAnimated by animateFloatAsState(targetValue = scale)
            val backgroundColorAnimated by animateColorAsState(if (index == currentPage) activeColor else inactiveColor)
            val textColorAnimated by animateColorAsState(if (index == currentPage) activeTextColor else inactiveTextColor)
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scaleAnimated
                        scaleY = scaleAnimated
                    }
                    .clip(indicatorShape)
                    .size(indicatorSize)
                    .background(
                        color = backgroundColorAnimated,
                        shape = indicatorShape
                    )
                    .then(modifier)
            ) {
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.Center),
                    visible = distance <= 1 && pageCount > 3,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    TextAutoSize(
                        modifier = Modifier.padding(horizontal = Margin.t),
                        text = "${index + 1}",
                        color = textColorAnimated,
                        fontWeight = FontWeight.SemiBold,
                        minSize = 6.sp,
                        maxSize = 12.sp
                    )
                }
            }
        }
    }
}
package own.moderpach.extinguish.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

object ExtinguishCardDefault {
    val shape @Composable get() = MaterialTheme.shapes.extraLarge
    val containerColor @Composable get() = MaterialTheme.colorScheme.surfaceContainerLow
    val contentColor @Composable get() = MaterialTheme.colorScheme.onSurface
    val tonalElevation = 1.dp
    val shadowElevation = 0.dp
    val border @Composable get() = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    val contentPadding = PaddingValues(0.dp)
    val horizontalAlignment: Alignment.Horizontal = Alignment.Start
    val verticalArrangement: Arrangement.Vertical = Arrangement.Top
}

@Composable
fun ExtinguishCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = ExtinguishCardDefault.shape,
    containerColor: Color = ExtinguishCardDefault.containerColor,
    contentColor: Color = ExtinguishCardDefault.contentColor,
    tonalElevation: Dp = ExtinguishCardDefault.tonalElevation,
    shadowElevation: Dp = ExtinguishCardDefault.shadowElevation,
    border: BorderStroke? = ExtinguishCardDefault.border,
    contentPadding: PaddingValues = ExtinguishCardDefault.contentPadding,
    horizontalAlignment: Alignment.Horizontal = ExtinguishCardDefault.horizontalAlignment,
    verticalArrangement: Arrangement.Vertical = ExtinguishCardDefault.verticalArrangement,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "press_progress"
    )

    // Morph the shape dynamically when pressed, rounding flat corners for an expressive feel
    val animatedShape = remember(shape, pressProgress) {
        if (shape is RoundedCornerShape && onClick != null) {
            object : Shape {
                override fun createOutline(
                    size: Size,
                    layoutDirection: LayoutDirection,
                    density: Density
                ): Outline {
                    val targetPx = with(density) { 22.dp.toPx() } // Align with MaterialTheme.shapes.extraLarge (22.dp)
                    fun lerp(start: Float, stop: Float, fraction: Float) =
                        (1 - fraction) * start + fraction * stop

                    val ts = lerp(shape.topStart.toPx(size, density), targetPx, pressProgress)
                    val te = lerp(shape.topEnd.toPx(size, density), targetPx, pressProgress)
                    val bs = lerp(shape.bottomStart.toPx(size, density), targetPx, pressProgress)
                    val be = lerp(shape.bottomEnd.toPx(size, density), targetPx, pressProgress)

                    return Outline.Rounded(
                        RoundRect(
                            rect = Rect(0f, 0f, size.width, size.height),
                            topLeft = androidx.compose.ui.geometry.CornerRadius(ts),
                            topRight = androidx.compose.ui.geometry.CornerRadius(te),
                            bottomRight = androidx.compose.ui.geometry.CornerRadius(be),
                            bottomLeft = androidx.compose.ui.geometry.CornerRadius(bs)
                        )
                    )
                }
            }
        } else shape
    }

    val clickableCheckedModifier = onClick?.let {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            onClick = it
        )
    } ?: Modifier

    Surface(
        modifier = modifier.clip(animatedShape),
        shape = animatedShape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
    ) {
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .then(clickableCheckedModifier),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}
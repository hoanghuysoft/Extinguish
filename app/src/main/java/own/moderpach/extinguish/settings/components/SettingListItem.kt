package own.moderpach.extinguish.settings.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SettingListItem(
    modifier: Modifier = Modifier,
    headline: String,
    supporting: String? = null,
    icon: ImageVector? = null,
    iconContainerColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer,
    iconColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer,
    leadingContent: @Composable() (RowScope.() -> Unit)? = null,
    trailingContent: @Composable() (RowScope.() -> Unit)? = null,
    shape: Shape = LocalSettingShape.current,
    cardContainerColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceContainerHigh,
    onClick: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "press"
    )

    val animatedShape = remember(shape, pressProgress) {
        if (shape is RoundedCornerShape && onClick != null) {
            object : Shape {
                override fun createOutline(
                    size: Size,
                    layoutDirection: LayoutDirection,
                    density: Density
                ): Outline {
                    val targetPx = with(density) { 20.dp.toPx() }
                    fun lerp(start: Float, stop: Float, fraction: Float) =
                        (1 - fraction) * start + fraction * stop

                    val ts = lerp(shape.topStart.toPx(size, density), targetPx, pressProgress)
                    val te = lerp(shape.topEnd.toPx(size, density), targetPx, pressProgress)
                    val bs = lerp(shape.bottomStart.toPx(size, density), targetPx, pressProgress)
                    val be = lerp(shape.bottomEnd.toPx(size, density), targetPx, pressProgress)

                    return Outline.Rounded(
                        androidx.compose.ui.geometry.RoundRect(
                            rect = androidx.compose.ui.geometry.Rect(
                                0f, 0f, size.width, size.height
                            ),
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(animatedShape),
        shape = animatedShape,
        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        val clickableModifier = if (onClick != null) {
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            )
        } else Modifier

        ListItem(
            headlineContent = {
                Text(
                    text = headline,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = supporting?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            leadingContent = {
                if (icon != null) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(iconContainerColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else if (leadingContent != null) {
                    Row { leadingContent() }
                }
            },
            trailingContent = trailingContent?.let {
                { Row { it() } }
            },
            modifier = clickableModifier.padding(vertical = 4.dp),
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}

@Composable
fun SettingListItemWithSwitch(
    modifier: Modifier = Modifier,
    headline: String,
    supporting: String? = null,
    icon: ImageVector? = null,
    iconContainerColor: Color = Color.Transparent,
    iconColor: Color = Color.Unspecified,
    shape: Shape = RoundedCornerShape(20.dp),
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) = SettingListItem(
    modifier = modifier,
    headline = headline,
    supporting = supporting,
    icon = icon,
    iconContainerColor = iconContainerColor,
    iconColor = iconColor,
    shape = shape,
    trailingContent = {
        Switch(checked, onCheckedChange)
    },
    onClick = {
        onCheckedChange(!checked)
    }
)

@Composable
fun SettingListItemWithSlider(
    modifier: Modifier = Modifier,
    overline: String,
    leadingContent: @Composable() (RowScope.() -> Unit)? = null,
    supporting: String? = null,
    icon: ImageVector? = null,
    iconContainerColor: Color = Color.Transparent,
    iconColor: Color = Color.Unspecified,
    shape: Shape = RoundedCornerShape(20.dp),
    value: Float,
    steps: Int = 0,
    onValueChangeFinished: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>
) {
    var tempValue by remember {
        mutableFloatStateOf(value)
    }
    SettingListItem(
        modifier = modifier,
        headline = overline,
        supporting = supporting,
        icon = icon,
        iconContainerColor = iconContainerColor,
        iconColor = iconColor,
        shape = shape,
        leadingContent = leadingContent,
        trailingContent = {
            Text("${(tempValue * 100).roundToInt()}%")
        }
    )
    // The slider could be placed below the item if we modify it, but for compatibility let's add it below
    Slider(
        modifier = Modifier.padding(horizontal = 16.dp),
        value = tempValue,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = {
            onValueChangeFinished(tempValue)
        },
        onValueChange = {
            tempValue = it
        }
    )
}
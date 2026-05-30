package own.moderpach.extinguish.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import own.moderpach.extinguish.ui.components.ExtinguishCard
import own.moderpach.extinguish.ui.components.ExtinguishCardDefault
import own.moderpach.extinguish.ui.components.ExtinguishListItem
import own.moderpach.extinguish.ui.theme.ExtinguishTheme

import own.moderpach.extinguish.settings.components.LocalSettingShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun RadioCard(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: (() -> Unit)?,
    headline: String,
    supporting: String?
) {
    val shape = LocalSettingShape.current
    val border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    
    Card(
        modifier = modifier.fillMaxWidth().clip(shape),
        shape = shape,
        border = border,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        onClick = { onClick?.invoke() },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(headline)
            },
            trailingContent = {
                RadioButton(selected, null)
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
        supporting?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun RadioCardPreview() = ExtinguishTheme {
    RadioCard(
        selected = true,
        headline = "ABCD",
        onClick = {},
        supporting = "Test text".repeat(25)
    )
}
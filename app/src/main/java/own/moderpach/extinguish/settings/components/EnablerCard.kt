package own.moderpach.extinguish.settings.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import own.moderpach.extinguish.ui.components.ExtinguishCard
import own.moderpach.extinguish.ui.components.ExtinguishListItem
import own.moderpach.extinguish.settings.components.SettingGroup

@Composable
fun EnablerCard(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    icon: ImageVector? = null,
    iconContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onCheckedChange: (Boolean) -> Unit
) {
    SettingGroup(
        {
            SettingListItemWithSwitch(
                modifier = modifier,
                headline = text,
                checked = checked,
                icon = icon,
                iconContainerColor = iconContainerColor,
                iconColor = iconColor,
                onCheckedChange = onCheckedChange
            )
        }
    )
}
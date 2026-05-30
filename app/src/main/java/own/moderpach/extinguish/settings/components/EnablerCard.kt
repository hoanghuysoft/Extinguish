package own.moderpach.extinguish.settings.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import own.moderpach.extinguish.ui.components.ExtinguishCard
import own.moderpach.extinguish.ui.components.ExtinguishListItem

@Composable
fun EnablerCard(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    SettingCard {
        SettingListItemWithSwitch(
            modifier = modifier,
            headline = text,
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
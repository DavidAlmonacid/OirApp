package com.example.oirapp.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(apiLevel = 28, showBackground = true)
annotation class CustomPreview

@Preview(
    name = "Light Mode",
    group = "UI mode",
    apiLevel = 28,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "Dark Mode",
    group = "UI mode",
    apiLevel = 28,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
annotation class DarkLightPreviews

@Preview(
    name = "Light Mode",
    group = "UI mode screens",
    apiLevel = 28,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:pixel_5",
)
@Preview(
    name = "Dark Mode",
    group = "UI mode screens",
    apiLevel = 28,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:pixel_5",
)
annotation class DarkLightScreenPreviews

@Preview(
    name = "Light Mode",
    group = "UI mode screens landscape",
    apiLevel = 28,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:parent=pixel_5,orientation=landscape",
)
@Preview(
    name = "Dark Mode",
    group = "UI mode screens landscape",
    apiLevel = 28,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:parent=pixel_5,orientation=landscape",
)
annotation class DarkLightLandscapeScreenPreviews

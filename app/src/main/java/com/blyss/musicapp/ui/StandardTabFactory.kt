package com.blyss.musicapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blyss.musicapp.data.TabType
import com.blyss.musicapp.data.Theme

object StandardTabFactory {
    val buttonSize = 36.dp

    lateinit var theme: Theme

    @Composable
    fun StandardTab(
        tabType: TabType,
        mainText: String,
        secondaryText: String = "",
        tertiaryText: String = "",
        onClick: () -> Unit = {},
        icon: Int? = null,
        background: Int? = null,
        buttons: List<@Composable (() -> Unit)>? = null,
    ) {
        var useIcon = tabType.icon()
        if (icon != null) {
            useIcon = icon
        }

        Row(
            modifier = Modifier
                .height(theme.tabSizeVertical.dp)
                .fillMaxWidth()
                .padding(horizontal = theme.tabPadding.dp, vertical = theme.tabPadding.dp)
                .clip(RoundedCornerShape(theme.tabRoundedCornerShape.dp))
                .background(theme.tabColor)
                .alpha(theme.tabAlpha),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(useIcon),
                contentDescription = tabType.toString(),
                modifier = Modifier
                    .height((theme.tabSizeVertical * 0.6).dp)
                    .aspectRatio(1F)
                    .padding(end = 8.dp),
                tint = theme.textColor
            )

            // Middle: Texts (clickable area)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick() }
            ) {
                Text(
                    text = mainText,
                    fontSize = theme.textSize.sp,
                    color = theme.textColor,
                    maxLines = 1
                )
                Text(
                    text = secondaryText,
                    color = theme.textColor,
                    maxLines = 1
                )
            }

            // Right: Tertiary text
            if (tertiaryText.isNotBlank()) {
                Text(
                    text = tertiaryText,
                    color = theme.textColor,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    maxLines = 1
                )
            }

            // Buttons
            if (buttons != null) {
                for (button in buttons) {
                    Box(
                        modifier = Modifier
                            .height(buttonSize)
                            //.weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        button()
                    }
                }
            }
        }
    }
}
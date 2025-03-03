package com.wceng.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.wceng.core.designsystem.R

@Composable
fun CardRectAngle(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CardColors = CardDefaults.cardColors(),
    onClick: () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Card(
        shape = RectangleShape,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = modifier,
        colors = colors,
        onClick = onClick,
        enabled =  enabled
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.horizontal_padding),
                vertical = dimensionResource(R.dimen.vertical_padding)
            )
        ) {
            content(modifier)
        }
    }
}


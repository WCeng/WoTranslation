package com.wceng.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WoBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit){

    Surface(
        modifier = modifier,
        tonalElevation = 4.dp
    ) {
        content()
    }
//    Box(
//        Modifier.background(Color.Blue)
//    ){
//        content()
//    }
}
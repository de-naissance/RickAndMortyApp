package com.example.rickandmortyapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Locale

/**
 * Filling the background color of the text depending on the status of the character
 */
@Composable
fun StatusIcon(
    status: String
) {
    /** Color selection depends on the status of the character */
    val tint by animateColorAsState(
        when(status) {
            "Alive" -> Color(0xFF00BC00)
            "Dead" -> Color(0xFFFF0000)
            else -> Color(0xFFBCBCBC)
        }
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(tint),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            color = Color.White,
            modifier = Modifier.padding(3.dp),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
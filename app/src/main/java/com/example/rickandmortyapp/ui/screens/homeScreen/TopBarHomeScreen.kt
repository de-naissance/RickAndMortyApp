package com.example.rickandmortyapp.ui.screens.homeScreen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.ui.LayoutUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    selectLayout: (Boolean) -> Unit,
    layoutUiState: LayoutUiState,
    navigateToSearch: () -> Unit,
    darkMode: Boolean,
    selectMode: (Boolean) -> Unit
) {
    val isLinearLayout = layoutUiState.isLinearLayout

    TopAppBar(
        title = { Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        ) },
        modifier = modifier,
        actions = {
            IconButton(
                onClick = {
                    navigateToSearch()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.manage_search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = {
                    selectLayout(!isLinearLayout)
                }
            ) {
                Icon(
                    painter = painterResource(id = layoutUiState.toggleIcon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            DropdownMenuTopBar(
                darkMode = darkMode,
                selectMode = selectMode
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun DropdownMenuTopBar(
    darkMode: Boolean,
    selectMode: (Boolean) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            showMenu = !showMenu
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.more_vert),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        DropdownMenuItem(
            text = { Text("Dark mode") },
            onClick = { selectMode(!darkMode) },
            trailingIcon = { CustomSwitch(darkMode = darkMode) }
        )
    }
    Log.d("isDarkMode", darkMode.toString())
}

@Composable
fun CustomSwitch(
    width: Dp = 72.dp,
    height: Dp = 40.dp,
    checkedTrackColor: Color = Color(0xFF35898F),
    uncheckedTrackColor: Color = Color(0xFFe0e0e0),
    gapBetweenThumbAndTrackEdge: Dp = 8.dp,
    borderWidth: Dp = 4.dp,
    cornerSize: Int = 50,
    iconInnerPadding: Dp = 4.dp,
    thumbSize: Dp = 24.dp,
    darkMode: Boolean
) {
    // for moving the thumb
    val alignment by animateAlignmentAsState(if (darkMode) 1f else -1f)

    // outer rectangle with border
    Box(
        modifier = Modifier
            .size(width = width, height = height)
            .border(
                width = borderWidth,
                color = if (darkMode) checkedTrackColor else uncheckedTrackColor,
                shape = RoundedCornerShape(percent = cornerSize)
            )/*
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                switchOn = !switchOn
            }*/,
        contentAlignment = Alignment.Center
    ) {

        // this is to add padding at the each horizontal side
        Box(
            modifier = Modifier
                .padding(
                    start = gapBetweenThumbAndTrackEdge,
                    end = gapBetweenThumbAndTrackEdge
                )
                .fillMaxSize(),
            contentAlignment = alignment
        ) {

            // thumb with icon
            Icon(
                painter = painterResource(
                    if (darkMode) R.drawable.light_mode else R.drawable.dark_mode
                ),
                contentDescription = if (darkMode) "Enabled" else "Disabled",
                modifier = Modifier
                    .size(size = thumbSize)
                    .background(
                        color = if (darkMode) checkedTrackColor else uncheckedTrackColor,
                        shape = CircleShape
                    )
                    .padding(all = iconInnerPadding),
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
private fun animateAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment> {
    val bias by animateFloatAsState(targetBiasValue)
    return remember { derivedStateOf { BiasAlignment(horizontalBias = bias, verticalBias = 0f) } }
}
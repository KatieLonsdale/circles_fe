package com.katielonsdale.chatterbox.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavHostController = rememberNavController(),
    scrollBehavior: TopAppBarScrollBehavior,
    loggedIn: Boolean,
){
    val containerColor: Color
    val titleContentColor: Color

    if (loggedIn) {
        containerColor = MaterialTheme.colorScheme.background
        titleContentColor = MaterialTheme.colorScheme.primary
    } else {
        containerColor = MaterialTheme.colorScheme.secondary
        titleContentColor = MaterialTheme.colorScheme.onSecondary
    }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = containerColor,
            titleContentColor = titleContentColor,
        ),
        title = {
            if(loggedIn) {
                Text(
                    "ChatterBox",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                )
            }
        },
        navigationIcon = {
            BackButton(
                onClickBack = { navController.popBackStack() },
                tint = titleContentColor,
            )
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewTopAppBar(){
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    ChatterBoxTheme {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            loggedIn = false,
        )
    }
}
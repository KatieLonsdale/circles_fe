package com.katielonsdale.chatterbox.ui.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.katielonsdale.chatterbox.ui.InnerCirclesScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.katielonsdale.chatterbox.theme.ChatterBoxTheme
import com.katielonsdale.chatterbox.R


@Composable
fun CreateNewFloatingActionButton(
    navController: NavHostController
) {
    FloatingActionButton(
        onClick = {
            navController.navigate(InnerCirclesScreen.CreateNew.name)
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.background,
        shape = MaterialTheme.shapes.small
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.add_fab
            ),
            contentDescription = "Add"
        )
    }
}

@Preview()
@Composable
fun PreviewCreateNewFloatingActionButton(){
    val navController = rememberNavController()
    ChatterBoxTheme {
        CreateNewFloatingActionButton(
            navController = navController
        )
    }
}
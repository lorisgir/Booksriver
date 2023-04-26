package com.example.booksriver.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp

@Composable
fun BooksriverBottomNavigation(
    onBottomNavigationClick: (item: BottomNavItem) -> Unit,
    currentRoute: String?
) {
    val isDarkTheme = isSystemInDarkTheme()

    BottomNavigation(
        modifier = Modifier.graphicsLayer {
            //shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            //clip = true
        },
        backgroundColor = if (isDarkTheme) MaterialTheme.colors.primary else Color.White,
        contentColor = Color.White
    ) {
        BottomNavItem.getItems().forEach { item ->
            val selected = currentRoute?.contains(item.screenName)
            BottomNavigationItem(icon = {
                Icon(
                    imageVector = if (selected == true) item.iconSelected else item.iconUnselected,
                    contentDescription = stringResource(item.titleId)
                )
            },
                label = {
                    Text(text = stringResource(item.titleId), fontSize = 9.sp)
                },
                selectedContentColor = if (isDarkTheme) Color.White else MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = true,
                selected = selected == true,
                onClick = { onBottomNavigationClick(item) })
        }
    }
}

@Composable
fun BooksriverRailNavigation(
    onBottomNavigationClick: (item: BottomNavItem) -> Unit,
    currentRoute: String?
) {
    val isDarkTheme = isSystemInDarkTheme()

    NavigationRail(
        modifier = Modifier.graphicsLayer {
            //shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            //clip = true
        },
        backgroundColor = if (isDarkTheme) MaterialTheme.colors.primary else Color.White,
        contentColor = Color.White
    ) {
        BottomNavItem.getItems().forEach { item ->
            val selected = currentRoute?.contains(item.screenName)
            NavigationRailItem(icon = {
                Icon(
                    imageVector = if (selected == true) item.iconSelected else item.iconUnselected,
                    contentDescription = stringResource(item.titleId)
                )
            },
                label = {
                    Text(text = stringResource(item.titleId), fontSize = 9.sp)
                },
                selectedContentColor = if (isDarkTheme) Color.White else MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = true,
                selected = selected == true,
                onClick = { onBottomNavigationClick(item) })
        }
    }
}

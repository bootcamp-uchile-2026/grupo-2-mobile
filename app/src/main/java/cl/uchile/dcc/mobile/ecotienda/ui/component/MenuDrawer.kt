package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ScreenRoutes
import cl.uchile.dcc.mobile.ecotienda.viewmodel.MainScreenViewModel

@Composable
fun MenuDrawer(
    viewModel: MainScreenViewModel,
    onCloseDrawer: () -> Unit
) {
    val currentRoute by viewModel.currentRoute.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = currentRoute.title,
            modifier = Modifier
                .padding(bottom = 16.dp),
            style = MaterialTheme.typography.headlineMedium
        )

        Divider()

        val items = listOf(
            ScreenRoutes.HOME,
            ScreenRoutes.CATALOG,
        )

        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(text = item.title) },
                selected = currentRoute == item,
                onClick = {
                    viewModel.navigateTo(item.route)
                    onCloseDrawer()
                },
            )
        }
    }
}
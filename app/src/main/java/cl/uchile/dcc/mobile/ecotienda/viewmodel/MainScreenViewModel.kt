package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ScreenEnum


class MainScreenViewModel : ViewModel() {
    var actualScreen by mutableStateOf(ScreenEnum.HOME)
        private set

    fun changeScreen(newScreen: ScreenEnum): ScreenEnum {
        actualScreen = newScreen
        return actualScreen
    }
}
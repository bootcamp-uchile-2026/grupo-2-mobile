# Fix Navigation and ViewModel State in EcoTiendaApp

The user is experiencing issues with `val currentRoute by viewModel.currentRoute.collectAsState()`. This is due to several reasons:
1. `viewModel` is being accessed as an object, but it is actually a function imported from `androidx.lifecycle.viewmodel.compose.viewModel`. The correct instance variable is `screenViewModel`.
2. The rest of the `EcoTiendaApp.kt` file uses `currentScreen` instead of `currentRoute`, leading to undefined variable errors.
3. There is a mismatch between `Routes` (used in `MainScreenViewModel`) and `ScreenEnum` (used in `EcoTiendaApp.kt`).
4. `MainScreenViewModel` is missing the `changeScreen` method (it has `navigateTo` which takes `Routes`).
5. `BottomNavigationBar` has a signature mismatch with how it's called.

## Proposed Changes

### [Component: UI Screen]

#### [MODIFY] [EcoTiendaApp.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/ui/screen/EcoTiendaApp.kt)
- Fix the state collection line to use `screenViewModel` and rename the variable to `currentScreen` to match usage.
- Update all `ScreenEnum` references to `Routes`.
- Update `screenViewModel.changeScreen` to `screenViewModel.navigateTo`.
- Fix the `BottomNavigationBar` call to match its signature (or update the signature).

#### [MODIFY] [BottomNavigationBar.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/ui/component/BottomNavigationBar.kt)
- Update the signature to accept `Routes` instead of `String` for better type safety and consistency.

### [Component: ViewModel]

#### [MODIFY] [MainScreenViewModel.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/viewmodel/MainScreenViewModel.kt)
- Ensure it uses `Routes` consistently (it already does, but I'll check if any helper methods are needed).

## Verification Plan

### Manual Verification
- Verify the code compiles without errors.
- Check that navigation works by clicking on the search bar and bottom navigation items.

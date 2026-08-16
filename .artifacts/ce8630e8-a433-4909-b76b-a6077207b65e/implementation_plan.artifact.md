# Plan para corregir el crash en la navegación al Catálogo

El usuario reporta que la aplicación se cierra al intentar navegar al Catálogo desde la barra inferior. Esto se debe a una inconsistencia entre los nombres de las constantes del enum `Routes` y los valores de su propiedad `route`, sumado al uso de `valueOf()` en el ViewModel.

## Análisis del Problema
1.  **Causa del Crash**: `MainScreenViewModel.kt` utiliza `Routes.valueOf(route)`. Cuando se navega desde la `BottomNavigationBar`, se envía `section.route`. Para el catálogo, este valor es `"Catálogo"`, pero `valueOf` espera `"CATALOG"`, lo que lanza un `IllegalArgumentException`.
2.  **Inconsistencia**: `Routes.HOME` tiene `route = "HOME"`, por lo que funciona. Otros como `CATALOG`, `SEARCH` ("Buscador"), etc., fallarán si se usa su propiedad `route`.
3.  **Estado del Catálogo**: El componente `Catalog` en `Catalog.kt` está vacío y su parámetro `modifier` es obligatorio.

## Cambios Propuestos

### ViewModel de la Pantalla Principal

#### [MODIFY] [MainScreenViewModel.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/viewmodel/MainScreenViewModel.kt)
- Modificar `navigateTo` para que busque la ruta comparando tanto con el nombre del enum como con la propiedad `route`, evitando así el crash por `valueOf()`.

### Integración en la App

#### [MODIFY] [EcoTiendaApp.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/ui/screen/EcoTiendaApp.kt)
- Asegurar que al llamar a `Catalog`, se le pase `Modifier.padding(innerPadding)` para que respete el espacio de las barras del sistema y no se vea "debajo" de la barra de búsqueda si se llegara a implementar contenido.

### Componente Catálogo (Opcional pero recomendado)

#### [MODIFY] [Catalog.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/ui/screen/Catalog.kt)
- Añadir un contenedor básico (ej. `Box`) que use el `modifier` para evitar errores visuales futuros.

## Plan de Verificación

### Verificación Manual
- Navegar a la sección de Catálogo desde la barra inferior y verificar que la app no se cierre.
- Probar la navegación desde la barra de búsqueda (que usa el nombre "SEARCH") y verificar que también funcione.

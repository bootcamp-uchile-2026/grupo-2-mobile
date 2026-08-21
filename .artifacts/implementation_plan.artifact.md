# Navegación y Detalle de Productor (Actualizado)

Implementaremos la navegación al detalle de un productor. He tomado nota de que el sistema de navegación debe usar los strings de `route` definidos en el enum, no solo el nombre del enum, para mantener consistencia con el resto de la app.

## User Review Required

> [!IMPORTANT]
> He detectado una inconsistencia: el `MainScreenViewModel` usa `Routes.valueOf(route)`, pero algunos strings de ruta (como "Página de Productores") no coinciden con el nombre del Enum (`PRODUCERPAGE`). Corregiremos esto para que la navegación sea robusta.

## Cambios Propuestos

### [Componente ViewModel]

#### [MODIFY] [MainScreenViewModel.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/viewmodel/MainScreenViewModel.kt)
- Añadir `selectedProducer` para persistir la selección.
- Actualizar `navigateTo` para que busque en el enum por la propiedad `.route`.
- Añadir `navigateToProducer(producer: Producer)` para simplificar el flujo.

### [Pantallas]

#### [NEW] [ProducerPage.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/ui/screen/ProducerPage.kt)
- Pantalla de detalle con historia, ubicación e imagen.

#### [MODIFY] [About.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/ui/screen/About.kt)
- Pasar el `Producer` seleccionado al hacer clic en el card.

#### [MODIFY] [EcoTiendaApp.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/ui/screen/EcoTiendaApp.kt)
- Inyectar el estado del productor seleccionado y mostrar `ProducerPage`.

## Plan de Verificación

### Verificación Manual
- Navegar a "Productores".
- Clic en un productor.
- Verificar que la pantalla detalle muestra la info correcta.
- Volver atrás y verificar que funciona.

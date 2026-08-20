# Centrar flecha de navegación con la imagen del producto

El objetivo es posicionar la flecha de navegación (derecha) para que aparezca centrada verticalmente respecto a la imagen de la tarjeta de producto en la pantalla de Inicio (Home).

## User Review Required

> [!IMPORTANT]
> Actualmente, la flecha se posiciona en el centro de la pantalla completa porque el contenedor `Box` principal usa `fillMaxSize()`. Se propone cambiar la estructura para que la flecha se alinee específicamente con el área del `HorizontalPager`.

## Proposed Changes

### UI Component: Home

#### [MODIFY] [Home.kt](file:///Users/luispereira/Desarrollo/Bootcamp/grupo-2-mobile/grupo-2-mobile/app/src/main/java/cl/uchile/dcc/mobile/ecotienda/ui/screen/Home.kt)

- Envolver el `HorizontalPager` y el `IconButton` (flecha) en un `Box` intermedio que no fuerce el centrado en toda la pantalla, sino que se ajuste a la altura del Pager.
- Ajustar el `contentPadding` del `HorizontalPager` para que sea simétrico (añadiendo `bottom = 16.dp`), asegurando que el centro vertical del Pager coincida con el centro vertical de la tarjeta de producto.
- Utilizar `Alignment.CenterEnd` dentro de este nuevo `Box` para la flecha.
- Cambiar el contenedor raíz a un `Column` para permitir futuros elementos debajo del carrusel sin afectar el posicionamiento de la flecha.

## Verification Plan

### Manual Verification
- Desplegar la aplicación y verificar que la flecha aparece al costado derecho, alineada con el centro de la tarjeta de producto.
- Cambiar entre productos para asegurar que la flecha aparece/desaparece correctamente según la posición en la lista.

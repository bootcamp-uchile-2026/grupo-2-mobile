# Buen Origen — Mobile

README actualizado (copia y pega)
Puedes reemplazar el contenido completo de README.md con esto:
Markdown# Buen Origen — Mobile

Aplicación Android desarrollada por el equipo Mobile del Grupo 2 para el Caso 2: EcoTienda (Buen Origen), en el Bootcamp de la Universidad de Chile (DCC).

Buen Origen es un prototipo de comercio electrónico orientado al consumo consciente. Esta versión mobile permite explorar productos sustentables, revisar su detalle, conocer emprendedores, administrar un carrito y recorrer flujos de acceso.

Repositorio: [bootcamp-uchile-2026/grupo-2-mobile](https://github.com/bootcamp-uchile-2026/grupo-2-mobile)

## Estado del proyecto

Versión mobile navegable construida con **Kotlin + Jetpack Compose (Material 3)**.

Actualmente se cuenta con:
- Estructura de navegación completa (bottom bar + stack).
- Capa de datos local en desarrollo (Entities, DAO y Repository) con persistencia SQLite/Room.
- Datos de catálogo preparados y listos para conectar a la API de Backend cuando esté disponible.
- Tema Material Design (colores, tipografías y formas) aplicado.
- Configuraciones locales en progreso (DataStore / SharedPreferences).

**Nota:** La API de Backend aún no está lista. Mientras tanto se utilizan datos locales / mocks. La arquitectura está preparada para intercambiar la fuente de datos sin rehacer las pantallas.

Application ID: `cl.uchile.dcc.mobile.ecotienda`

## Funcionalidades

- Página de inicio con productos y acceso al detalle / carrito.
- Catálogo con categorías.
- Detalle de producto (cantidad + agregar al carrito).
- Sección de emprendedores y detalle de emprendedor.
- Carrito de compras.
- Login / acceso simulado.
- Navegación inferior (Inicio, Catálogo, Emprendedores, Carrito).
- Búsqueda (pantalla presente).

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + Lifecycle
- Room / SQLite (capa de datos local)
- DataStore (configuraciones locales — en progreso)
- Coil (carga de imágenes)
- Gradle Version Catalog

**SDK mínimo:** 24  
**Target SDK:** 36

## Cómo ejecutar el proyecto

**Requisitos:** Android Studio (versión reciente con AGP 9.x) + emulador o dispositivo con Android 7.0+.

## Entrega

Versión de entrega para Hito 2
Commit
https://github.com/bootcamp-uchile-2026/grupo-2-mobile/commit/27d7b3ae866d2c9ede3ba47cbc29af9db2d09d74
27d7b3ae866d2c9ede3ba47cbc29af9db2d09d74 

## Licencia

Consulta el archivo `LICENSE` incluido en este repositorio.

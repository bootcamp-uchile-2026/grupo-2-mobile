# Buen Origen — Mobile

Aplicación Android desarrollada por el equipo Mobile del Grupo 2 para el **Hito 3** del Caso 2: EcoTienda, en el Bootcamp de la Universidad de Chile (DCC).

Buen Origen es un prototipo de comercio electrónico orientado al consumo consciente. Esta versión mobile busca completar el **MVP** con el 100% de la funcionalidad definida, refinando componentes según los avances de UX/UI y Backend, e incorporando almacenamiento local donde se justifique.

Repositorio: [bootcamp-uchile-2026/grupo-2-mobile](https://github.com/bootcamp-uchile-2026/grupo-2-mobile)

## Estado del proyecto

Versión mobile navegable construida con **Kotlin** y **Jetpack Compose (Material 3)**.

**Avances del Hito 3:**
- Completar el MVP con el 100% de la funcionalidad definida.
- Refinamiento de componentes y navegación alineados con UX/UI y Backend.
- Uso de almacenamiento local (Room) en los casos que se justifiquen (carrito, caché, preferencias).
- Implementación de al menos una funcionalidad diferenciadora.
- Prototipo de aplicación desktop con **Kotlin Multiplatform (KMP)** reutilizando gran parte del código móvil.
- Documentación refinada.

El identificador de la aplicación es `cl.uchile.dcc.mobile.ecotienda`.

## Funcionalidades

- Página de inicio con productos y acceso al detalle o al carrito.
- Catálogo con categorías y filtro.
- Vista de detalle de producto.
- Sección de emprendedores y detalle de emprendedor.
- Carrito de compras **persistente** (Room).
- Búsqueda con filtro (en refinamiento).
- Acceso / Login.
- Navegación inferior (Inicio, Catálogo, Nosotros, Carrito).
- Funcionalidad diferenciadora (en implementación).

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + Lifecycle
- **Room + SQLite** (persistencia local)
- DataStore (configuraciones locales)
- Coil (carga de imágenes)
- **Kotlin Multiplatform (KMP)** — prototipo desktop
- Gradle Version Catalog

**SDK mínimo:** 24  
**Target SDK:** 36

## Cómo ejecutar el proyecto

**Requisitos:** Android Studio (versión reciente con AGP 9.x) + emulador o dispositivo con Android 7.0+.

```bash
git clone https://github.com/bootcamp-uchile-2026/grupo-2-mobile.git
cd grupo-2-mobile

# Buen Origen — Mobile

Aplicación Android desarrollada por el equipo Mobile del Grupo 2 para el **Hito 2** del Caso 2: EcoTienda, en el Bootcamp de la Universidad de Chile (DCC).

Buen Origen es un prototipo de comercio electrónico orientado al consumo consciente. Esta versión mobile permite explorar productos sustentables, revisar su detalle, conocer emprendedores, administrar un carrito con **persistencia local** y recorrer un acceso simulado.

Repositorio: [bootcamp-uchile-2026/grupo-2-mobile](https://github.com/bootcamp-uchile-2026/grupo-2-mobile)

## Estado del proyecto

Versión mobile navegable construida con **Kotlin** y **Jetpack Compose**.  
Para este hito se implementó **persistencia local con Room + SQLite**. Los datos se almacenan en base de datos local y se siembran inicialmente mediante un `DatabaseSeeder` (basado en los datos de demostración).

El identificador de la aplicación es `cl.uchile.dcc.mobile.ecotienda`.

## Funcionalidades

- Página de inicio con productos y acceso al detalle o al carrito.
- Catálogo con categorías (Mercado, Cuidado Personal, Huerto en Casa, Hogar, Bienestar, Moda Natural, Mascotas, Artesanías).
- Vista de detalle de producto con cantidad y agregado al carrito.
- Sección Nosotros con listado de emprendedores.
- Vista de detalle de emprendedor.
- Barra de búsqueda (pantalla presente; filtro aún pendiente de implementación completa).
- **Carrito persistente** (los productos se mantienen al cerrar la aplicación gracias a Room).
- Acceso simulado con validación de correo y contraseña.
- Navegación inferior entre Inicio, Catálogo, Nosotros y Carrito.
- Barra superior con acceso a búsqueda y a login.

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- AndroidX Lifecycle / ViewModel
- **Room + SQLite** (persistencia local)
- Coil 3 (carga de imágenes)
- Gradle Version Catalog (`gradle/libs.versions.toml`)

SDK mínimo: 24  
Target SDK: 36

## Cómo ejecutar el proyecto

**Requisitos:** Android Studio (versión reciente, con AGP 9.x) y un emulador o dispositivo con Android 7.0 o superior.

Clona el repositorio:
`git clone https://github.com/bootcamp-uchile-2026/grupo-2-mobile.git`

Abre la carpeta grupo-2-mobile en Android Studio.
Espera a que Gradle sincronice las dependencias.
Ejecuta la configuración app en un emulador o dispositivo.

Desde la terminal (con dispositivo o emulador conectado):
Bash./gradlew installDebug

Estructura del proyecto

<img width="692" height="540" alt="Captura de pantalla 2026-10-20 a la(s) 8 19 35 p m" src="https://github.com/user-attachments/assets/4d2d8168-bc0b-4195-ae50-16206315b954" />

Navegación implementada
Un único NavHost en EcoTiendaApp, con destino inicial HOME.
textEcoTiendaApp (NavHost)

<img width="423" height="198" alt="Captura de pantalla 2026-10-20 a la(s) 8 21 37 p m" src="https://github.com/user-attachments/assets/04c1fcd4-d798-4af1-a3a3-75f245a54244" />

Datos y persistencia

Los datos de demostración se cargan inicialmente mediante un DatabaseSeeder.
Se utiliza Room para persistir:
Productos
Productores / Emprendedores
Usuarios
Carrito

El carrito ahora sí se mantiene al cerrar y reabrir la aplicación.
Aún no hay integración con el backend real (los datos siguen siendo locales/simulados).

Alcance y limitaciones del Hito 2

Contenido e imágenes demostrativas.
Persistencia local implementada con Room (productos, productores, usuarios y carrito).
No hay integración con API/backend real.
El inicio de sesión es simulado (no autentica contra un servicio externo).
No hay checkout ni pago.
La búsqueda aún no filtra el catálogo de forma completa.
El registro (SignUp) aún no está conectado al flujo de navegación.

Equipo Mobile

Luis Pereira — Desarrollo Mobile

Metodología de trabajo
El equipo utiliza GitFlow:

main: versiones estables y entregables
develop: integración
feature/*: funcionalidades
release/*: preparación de entregas

Diseño
La implementación se basa en los wireframes del flujo comprador entregados por el equipo UX/UI.

Licencia
Consulta el archivo LICENSE incluido en este repositorio.

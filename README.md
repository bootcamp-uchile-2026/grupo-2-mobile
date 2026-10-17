# Buen Origen — Mobile

Aplicación Android desarrollada por el equipo Mobile del Grupo 2 para el Hito 1 del Caso 2: EcoTienda, en el Bootcamp de la Universidad de Chile (DCC).

Buen Origen es un prototipo de comercio electrónico orientado al consumo consciente. Esta versión mobile permite explorar productos sustentables, revisar su detalle, conocer emprendedores, administrar un carrito y recorrer un acceso simulado.

Repositorio: [bootcamp-uchile-2026/grupo-2-mobile](https://github.com/bootcamp-uchile-2026/grupo-2-mobile)

## Estado del proyecto

Versión mobile navegable construida con Kotlin y Jetpack Compose. Para este hito se utilizan datos en memoria (`DefaultData`) y estado local en ViewModels; no requiere conexión con un backend.

El identificador de la aplicación es `cl.uchile.dcc.mobile.ecotienda`.

## Funcionalidades

* Página de inicio con productos de muestra y acceso al detalle o al carrito.
* Catálogo con categorías (Mercado, Cuidado Personal, Huerto en Casa, Hogar, Bienestar, Moda Natural, Mascotas, Artesanías).
* Vista de detalle de producto con cantidad y agregado al carrito.
* Sección Nosotros con listado de emprendedores.
* Vista de detalle de emprendedor.
* Barra de búsqueda (pantalla presente; la consulta aún no filtra resultados).
* Carrito con productos agregados desde inicio, catálogo o detalle.
* Acceso simulado con validación de correo y contraseña.
* Navegación inferior entre Inicio, Catálogo, Nosotros y Carrito.
* Barra superior con acceso a búsqueda y a login.

## Tecnologías utilizadas

* Kotlin
* Jetpack Compose (Material 3)
* Navigation Compose
* AndroidX Lifecycle / ViewModel
* Coil 3 (carga de imágenes)
* Gradle Version Catalog (`gradle/libs.versions.toml`)

SDK mínimo: 24. Target SDK: 36.

## Cómo ejecutar el proyecto

Requisitos: Android Studio (versión reciente, con AGP 9.x) y un emulador o dispositivo con Android 7.0 o superior.

1. Clona el repositorio:

```bash
git clone https://github.com/bootcamp-uchile-2026/grupo-2-mobile.git
```

2. Abre la carpeta `grupo-2-mobile` en Android Studio.

3. Espera a que Gradle sincronice las dependencias.

4. Ejecuta la configuración `app` en un emulador o dispositivo.

Desde la terminal, con un dispositivo o emulador conectado:

```bash
./gradlew installDebug
```

La navegación hacia las demás vistas se realiza desde la propia aplicación.

El carrito vive en memoria mientras la app está abierta. Al cerrar el proceso se reinicia.

## Estructura del proyecto

```
grupo-2-mobile/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/cl/uchile/dcc/mobile/ecotienda/
│       │   ├── model/              # Product, Producer, Cart, DefaultData
│       │   ├── ui/
│       │   │   ├── MainActivity.kt
│       │   │   ├── component/      # Bottom bar, cards, search, secciones
│       │   │   ├── screen/         # Pantallas y NavHost (EcoTiendaApp)
│       │   │   ├── screenstates/   # Estados de UI
│       │   │   └── theme/
│       │   └── viewmodel/          # Auth, Cart, Catalog, Detail, Main
│       └── res/
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
├── LICENSE
└── README.md
```

Las pantallas no consultan una API. Los listados salen de `model/DefaultData.kt`. El carrito y el login se manejan en ViewModels.

## Navegación implementada

Un único `NavHost` en `EcoTiendaApp`, con destino inicial `HOME`. Las pestañas inferiores son Inicio, Catálogo, Nosotros y Carrito. Login y Búsqueda ocultan top bar y bottom bar.

```
EcoTiendaApp (NavHost)
├── HOME          → HomeEcoTienda
├── CATALOG       → Catalog
├── ABOUT         → About (emprendedores)
├── PRODUCERPAGE  → ProducerPage
├── PRODUCTPAGE   → ProductPage
├── CART          → Cart
├── SEARCH        → SearchScreen
└── LOGIN         → Login
```

Rutas definidas en `ui/screen/ScreenRoutes.kt`.

## Vistas principales

| Vista            | Archivo                         | Descripción                                              |
| ---------------- | ------------------------------- | -------------------------------------------------------- |
| Inicio           | `ui/screen/Home.kt`             | Presentación de productos y acceso al detalle o carrito. |
| Catálogo         | `ui/screen/Catalog.kt`          | Productos por categoría.                                 |
| Producto         | `ui/screen/ProductPage.kt`      | Detalle, cantidad y agregado al carrito.                 |
| Nosotros         | `ui/screen/About.kt`            | Emprendedores de la tienda.                              |
| Emprendedor      | `ui/screen/ProducerPage.kt`     | Detalle del productor seleccionado.                      |
| Carrito          | `ui/screen/Cart.kt`             | Productos agregados en la sesión.                        |
| Búsqueda         | `ui/screen/SearchScreen.kt`     | Pantalla de búsqueda (filtro pendiente).                 |
| Login            | `ui/screen/Login.kt`            | Acceso simulado con validación local.                    |
| Registro         | `ui/screen/SignUp.kt`           | Archivo presente; pantalla aún no conectada al NavHost.  |

## Datos y comportamiento

Los productos, precios y emprendedores son datos simulados definidos en `DefaultData`. El flujo de autenticación es una simulación de interfaz: valida formato de correo y largo de contraseña, no crea cuentas y no transmite información a servicios externos.

No hay persistencia en disco del carrito. El estado se mantiene en `CartViewModel` durante el ciclo de vida del proceso.

Cuando exista API, el punto de recambio es dejar de leer `DefaultData` y mover esa lectura a una capa de repositorio o servicio, sin rehacer las pantallas.

## Equipo Mobile

* Luis Pereira — Desarrollo Mobile

## Metodología de trabajo

El equipo utiliza GitFlow como estrategia de ramas:

* `main`: versiones estables y entregables.
* `develop`: integración del trabajo.
* `feature/*`: desarrollo de funcionalidades específicas.
* `release/*`: preparación y validación de una entrega.

Flujo previsto para el Hito 1:

```
feature/* → develop → release/* → main → tag
```

Repositorio: [bootcamp-uchile-2026/grupo-2-mobile](https://github.com/bootcamp-uchile-2026/grupo-2-mobile)

Tablero del proyecto: GitHub Project del Grupo 2

## Diseño

La implementación se basa en los wireframes del flujo comprador entregados por el equipo UX/UI. En mobile ese flujo se resuelve con barra inferior (Inicio, Catálogo, Nosotros, Carrito), detalle de producto, detalle de emprendedor y acceso simulado.

## Alcance y limitaciones del Hito 1

* El contenido y las imágenes son demostrativos.
* No existe integración con una API o base de datos.
* El inicio de sesión no autentica usuarios reales.
* No hay checkout ni pago en esta versión.
* El registro (`SignUp`) aún no forma parte del grafo de navegación.
* La búsqueda no aplica filtro sobre el catálogo.
* El carrito no se persiste al cerrar la aplicación.

## Entrega

La versión presentada debe corresponder al commit y al tag publicados en la rama `main`. El nombre del tag y el hash del commit se incorporarán cuando se genere la versión definitiva del Hito 1.

Presentación programada para el sábado 29 de agosto de 2026.

## Licencia

Consulta el archivo `LICENSE` incluido en este repositorio.

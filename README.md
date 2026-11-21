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

## Último Commit para Hito 3
[Update README.md](https://github.com/bootcamp-uchile-2026/grupo-2-mobile/commit/8460faf7358bcaef03c1e39ffd63ecb06ed86fa7)

## Cómo ejecutar el proyecto

**Requisitos:** Android Studio (versión reciente con AGP 9.x) + emulador o dispositivo con Android 7.0+.

```
bash
git clone https://github.com/bootcamp-uchile-2026/grupo-2-mobile.git
cd grupo-2-mobile
```

Abre la carpeta en Android Studio.
Espera a que Gradle sincronice.
Ejecuta la configuración app.

Desde terminal (con dispositivo/emulador conectado):
Bash./gradlew installDebug
Prototipo Desktop (KMP)
El prototipo desktop se encuentra en desarrollo. Una vez disponible, se documentarán los pasos específicos de ejecución en esta sección.
Estructura del proyecto

```
grupo-2-mobile/
├── app/
│   └── src/main/java/cl/uchile/dcc/mobile/ecotienda/
│       ├── data/           # Room, Repository, Entities, DAO
│       ├── model/
│       ├── ui/
│       │   ├── component/
│       │   ├── screen/
│       │   └── theme/
│       └── viewmodel/
└── ...
```
Capa de datos

Local (Room): Productos, Productores/Emprendedores, Usuarios y Carrito.
Remoto: Integración con la API de Backend (en progreso).
La arquitectura permite combinar o alternar fuentes de datos sin modificar las pantallas.
Se utiliza almacenamiento local de forma justificada (persistencia de carrito, caché, preferencias).

## Navegación
Un único NavHost en EcoTiendaApp:
```
TextoEcoTiendaApp (NavHost)
├── HOME           → Home
├── CATALOG        → Catalog
├── ABOUT          → About (emprendedores)
├── PRODUCERPAGE   → ProducerPage
├── PRODUCTPAGE    → ProductPage
├── CART           → Cart
├── SEARCH         → SearchScreen
└── LOGIN          → Login
```

## Alcance del Hito 3

Completar el MVP de la aplicación móvil (100% de la funcionalidad definida).
Refinar componentes y navegación según avances de UX/UI y Backend.
Implementar funcionalidad diferenciadora en la app móvil.
Crear prototipo de aplicación desktop con KMP reutilizando código del e-commerce móvil.
Refinar la documentación.

## Equipo Mobile

Luis Pereira — Desarrollo Mobile

## Metodología de trabajo
Se utiliza GitFlow:

main → versiones estables / entregables
develop → integración
feature/* → nuevas funcionalidades
release/* → preparación de entregas

##Diseño
La implementación se basa en los wireframes y refinamientos entregados por el equipo UX/UI.
Licencia
Consulta el archivo LICENSE incluido en este repositorio.
Texto
---

###Cómo actualizarlo

**Opción rápida (terminal):**

```bash
# En la raíz del repo
git checkout develop   # o la rama que uses
git pull

# Reemplaza el contenido de README.md con el de arriba

git add README.md
git commit -m "docs: actualizar README para Hito 3 (MVP, KMP y refinamientos)"
git push
```

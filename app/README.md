# GeoRegister

Aplicación Android para registrar, consultar y visualizar incidencias geolocalizadas.

## Funcionalidades

- Registro de incidencias con título, descripción, categoría y fecha.
- Selección de fecha y hora mediante calendario y reloj.
- Obtención de coordenadas mediante el GPS del dispositivo.
- Soporte para coordenadas positivas y negativas.
- Almacenamiento de incidencias en Firebase Cloud Firestore.
- Historial de incidencias en una lista.
- Visualización de las incidencias como marcadores en Google Maps.
- Detección de movimiento mediante el acelerómetro.

## Tecnologías

- Java
- Android SDK
- AndroidX AppCompat
- ConstraintLayout
- RecyclerView
- Google Maps SDK
- Google Play Services Location
- Firebase Cloud Firestore
- Gradle Kotlin DSL

## Estructura principal

```text
app/src/main/java/com/lira/georegister/
├── MainActivity.java
├── Main_RegistrarIncidencia.java
├── Main_Historial.java
├── Main_Mapa.java
├── Main_Sensores.java
└── IncidenciaAdapter.java
```

## Configuración

1. Abre el proyecto en Android Studio.
2. Configura un dispositivo físico o un emulador.
3. Coloca el archivo `google-services.json` dentro de `app/`.
4. Configura `MAPS_API_KEY` en `local.properties`:

   ```properties
   MAPS_API_KEY=TU_CLAVE_DE_GOOGLE_MAPS
   ```

5. En Firebase, habilita Cloud Firestore y configura sus reglas de acceso.
6. Sincroniza el proyecto con Gradle.

## Colección de Firestore

Las incidencias se guardan en la colección `incidencias` con los siguientes campos:

| Campo | Tipo |
|---|---|
| `titulo` | String |
| `descripcion` | String |
| `fecha` | String |
| `categoria` | String |
| `latitud` | Double |
| `longitud` | Double |

Las coordenadas deben estar dentro de estos rangos:

- Latitud: `-90` a `90`
- Longitud: `-180` a `180`

## Compilación

Desde la raíz del proyecto ejecuta:

```powershell
.\gradlew.bat :app:assembleDebug
```

El APK de depuración se genera en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Pantallas

- `MainActivity`: menú principal.
- `Main_RegistrarIncidencia`: formulario para crear incidencias.
- `Main_Historial`: lista de incidencias almacenadas.
- `Main_Mapa`: mapa con los marcadores de las incidencias.
- `Main_Sensores`: lectura del acelerómetro y detección de movimiento.

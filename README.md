# Reto colaborativo: consumir una API con token en Android

App en Kotlin que inicia sesión contra la API de DummyJSON, guarda el token y lo usa para pedir los datos del usuario. Hecha en pareja, cada uno en su computador, coordinados por este repositorio.

**Integrantes:** Miguel Angel Gomez Mariaca y Danna Sofia Muñoz Ramos 

## 1. Quién creó el repositorio y quién lo clonó

Miguel creó el proyecto en Android Studio, lo subió a GitHub y agregó a Danna como colaboradora (Settings → Collaborators). Danna aceptó la invitación y clonó el repositorio con `git clone` en su computador.

## 2. Opción de organización: B (división por paso + revisión cruzada)

Nos repartimos los pasos de la guía. Elegimos esta opción porque la guía se separa de forma natural en dos partes: la capa de datos y configuración, y la lógica de la pantalla. La segunda depende de la primera, así que cada quien tomó una parte y trabajamos en orden.

| Danna | Miguel |
|---|---|
| Permiso de Internet | Proyecto Android base |
| Dependencias (Retrofit, Gson, OkHttp, corrutinas) | Método de login |
| Modelos `LoginRequest`, `LoginResponse`, `UserResponse` | Consulta de usuario autenticado |
| Interfaz `ApiService` | Manejo del accessToken |
| `RetrofitClient` | Autorización Bearer Token |
| | Mostrar el resultado en la aplicación |
| | Pruebas y corrección del consumo de la API |

Revisión cruzada: Miguel agregó comentarios explicativos en `ApiService.kt` (de Danna) sobre `suspend` y `Response`. Danna agregó comentarios en `MainActivity.kt` (de Miguel) sobre `SharedPreferences` y `ocultarFormulario`.

## 3. Cómo avanzamos

**Guía base.** Cada commit se hizo en su propia rama y se integró a `main` con un Pull Request (merge commit). Danna terminó sus commits primero, porque el código de Miguel usa sus modelos, `ApiService` y `RetrofitClient`. Después Miguel hizo `git pull` y siguió con los suyos.

**Extensión.** Con la guía base funcionando agregamos, también en ramas y Pull Requests:

1. Pantalla de login con dos `EditText` y el botón "Ingresar", reemplazando las credenciales fijas.
2. Mensaje de error visible si el login falla (ver sección 5).
3. Guardado del token en `SharedPreferences` y salto del formulario si ya hay sesión guardada.

La extensión la implementó Miguel: pantalla de login, mensaje de error y guardado del token en `SharedPreferences`.

## 4. Dificultad al sincronizar

Al abrir un Pull Request, GitHub marcó conflictos en `.idea/gradle.xml`, `.idea/misc.xml` y `.idea/vcs.xml`. Son archivos de configuración de Android Studio que se colaron en los commits porque se usó `git add .`. Los resolvimos conservando la versión de `main` en cada archivo.

Además, al hacer `git pull`, Git se negó a traer los cambios porque en el computador había archivos de `.idea` sin seguimiento que iba a sobrescribir. Cerramos Android Studio, borramos la carpeta `.idea` local y repetimos el `git pull`. Desde ahí acordamos agregar solo los archivos de cada commit (por ruta) y revisar `git status` antes de commitear.

## 5. Reto final: mensaje visible cuando el login falla

Tocamos la función `hacerLogin` de `MainActivity.kt`, en la rama `else` del `if (resp.isSuccessful)`. Ese es el único punto donde se sabe que el servidor rechazó las credenciales, así que ahí mostramos el error. Allí mostramos el texto "Login falló: usuario o contraseña incorrectos" en el `TextView` de la pantalla y un `Toast`. En el `Toast` usamos `this@MainActivity` y no `this`, porque dentro de `lifecycleScope.launch { }` el `this` es la corrutina y no la pantalla, y `Toast` necesita el contexto de la pantalla. En el `catch` agregamos también un `Toast` de "Error de conexión" para cuando no hay internet. Lo probamos con una contraseña incorrecta a propósito (sale el mensaje en pantalla y no solo en el Logcat) y con `emilys` / `emilyspass` (sigue funcionando igual que antes).
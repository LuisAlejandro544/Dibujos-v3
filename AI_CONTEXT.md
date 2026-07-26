# 🤖 Manual de Contexto para Inteligencias Artificiales (AI_CONTEXT)

Este archivo sirve como **guía operativa obligatoria** para cualquier agente de IA o Asistente de Código que trabaje en el repositorio **KidsDraw Canvas**.

---

## 🎯 Propósito del Proyecto
KidsDraw Canvas es una aplicación móvil Android nativa en Kotlin (Jetpack Compose) diseñada para experiencias artísticas infantiles con alta interactividad, desarrollo modular, gestor de capas avanzado (con renombrado, combinación hacia abajo, opacidad y miniaturas live), sellos, **stickers con ojitos saltones 👀 y accesorios**, **pinceles con texturas mágicas (Estrellitas ⭐, Burbujas 🫧, Galaxia 🌌)**, **paletas de colores expandidas neón/pastel y mezclador mágico**, **herramienta de Pincel Doble**, **asistente de voz Text-To-Speech para niños que no saben leer**, **aceleración por GPU**, **modo horizontal adaptativo con riel de herramientas de acceso rápido**, **modo duelo de dibujo 1vs1 local con Juez Mágico AI (Gemini 2.5 Flash Vision & Reasoning)**, herramientas avanzadas de selección y persistencia local Room DB. Además, incorpora una arquitectura híbrida de rendimiento con extensiones JNI en C++ y Rust, e integración CI/CD con GitHub Actions.

---

## 📐 Reglas de Arquitectura Modular & Código

1. **Lenguaje Principal:** Kotlin exclusivo para la capa Android UI/Domain/Data.
2. **Estructura Modular del Lienzo y UI:**
   - `MainActivity.kt`: Mantiene la actividad principal ligera, encargada únicamente de la inicialización de servicios de voz y la navegación entre pantallas (`AppScreen`).
   - `CanvasScreen.kt`: Encapsula toda la interfaz composable del lienzo de dibujo, rieles de herramientas y modales de la aplicación.
   - `DrawingCanvas.kt`: Componente composable interactivo enfocado en la captura táctil y coordinación del viewport.
   - Renderizadores Modulares:
     - `CanvasPathRenderer.kt`: Lógica de dibujado de trazos (Pincel, Neón, Crayón, Pincel Doble, Borrador, Balde de Pintura).
     - `CanvasStampRenderer.kt`: Lógica de renderizado vectorial de figuras y sellos (Estrella, Corazón, Flor, Huella, etc.).
     - `CanvasSelectionOverlay.kt`: Lógica de renderizado del cuadro de selección lazo y tiradores.
   - `DrawingPersistenceManager.kt`: Gestor de serialización JSON Moshi y operaciones de persistencia Room de forma desacoplada.
3. **Framework de UI & Accesibilidad:**
   - Jetpack Compose con Material Design 3.
   - Las interacciones clave (cambios de herramienta, capas, colores, sellos) se notifican mediante `VoiceAssistant.speak()` cuando el modo de voz para niños está activado.
4. **Mapeo de Nombres en Version Catalog:**
   - En `gradle/libs.versions.toml`, las claves kebab-case (ej. `androidx-core-ktx`) se convierten a dot-notation en Gradle (`libs.androidx.core.ktx`).
5. **Módulos Nativos (C++ y Rust):**
   - La interacción con C++ y Rust debe canalizarse siempre a través de `com.example.nativeengine.NativeDrawingEngine`.
   - **FALLBACK OBLIGATORIO:** Cualquier método nativo JNI debe poseer un fallback equivalente implementado en puro Kotlin para garantizar que la app funcione sin errores en builds donde no se incluyan binarios precompilados de NDK.
6. **Manejo de Estado UI:**
   - Usar `CanvasViewModel` con `StateFlow<CanvasUiState>`.
   - Modificar estados de forma inmutable creando nuevas instancias de data classes (`copy()`).
7. **Persistencia Local:**
   - Room Database con KSP gestionado a través de `DrawingPersistenceManager`. Mantener `DrawingEntity` y `DrawingDao` actualizados.
8. **CI/CD & Firmado de APKs:**
   - El archivo `.github/workflows/build-apk.yml` gestiona las builds automáticas. Nunca commitear keystores privados al repositorio git; las claves de firma se generan en caliente durante el workflow con `keytool`.

---

## ⛔ Restricciones Estrictas
- **NO** eliminar ni modificar `metadata.json` excepto para actualizar el título o la descripción.
- **NO** cambiar la clave de `applicationId` ni el paquete `com.example` arbitrariamente.
- **NO** remover la capacidad `MAJOR_CAPABILITY_SERVER_SIDE_GEMINI_API` de `metadata.json`.
- **NO** usar `local.properties` para claves de API; usar variables de entorno o Secrets.

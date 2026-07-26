# KidsDraw Canvas 🎨✨

**KidsDraw Canvas** es una aplicación móvil nativa para Android diseñada para niños y pequeños artistas, combinando una interfaz sumamente interactiva, moderna y colorida con una arquitectura técnica híbrida de alto rendimiento.

---

## 🌟 Características Principales

- **🎨 Herramientas de Dibujo Mágicas:**
  - Lápiz, Pincel Suave, **Pincel Doble ✌️**, Crayón, Marcador de Neón, Borrador y Herramienta de Relleno (Balde de Pintura).
  - **Pincel Doble:** Trazo paralelo dinámico con síntesis de color complementario para efectos caligráficos y de diseño.
  - **Sello Mágico (Stamps):** Estampa estrellas, corazones, chispas, flores, huellas, soles y arcoíris.
  - **Modo Arcoíris Rainbow 🌈:** Trazos dinámicos con variaciones de matiz de color en tiempo real.
  - **Efecto Neón / Brillo ✨:** Resplandor brillante alrededor de cada trazo.

- **⚡ Aceleración por Hardware GPU (Pipeline C++ & Compose):**
  - Renderizado directo acelerado por hardware para trazos de pincel fluido, capas compuestas y baja latencia de respuesta táctil.
  - Indicador badge interactivo de estado de GPU en la barra superior.

- **📱 Modo Horizontal Adaptativo (Landscape Quick Tools):**
  - Barra lateral de acceso rápido (`LandscapeQuickToolsRail.kt`) optimizada para uso en orientación horizontal.
  - Permite cambiar de herramienta (Lápiz, Pincel, Pincel Doble, Crayón, Neón, Sellos, Balde, Borrador, Selección), seleccionar presores de grosor de trazo de 1 toque (Fino, Medio, Grueso, Gigante) y colores sin tapar el lienzo de dibujo.

- **⚔️ Modo Duelo de Dibujo 1vs1 Local con Juez Mágico AI (Gemini Vision & Reasoning):**
  - Dos niños pueden competir o colaborar turnándose en el mismo dispositivo.
  - **Generador AI de Temas Creativos:** Gemini genera frases disparatadas e imaginativas adaptadas para niños.
  - **Juez Mágico AI Multimodal:** Analiza visualmente ambas obras con el modelo `gemini-3.5-flash` (modo visión y razonamiento elevado), otorga estrellas, resalta detalles creativos en ambos dibujos y da un veredicto alegre narrado en voz alta por el Asistente de Voz.

- **🔊 Asistente de Voz para Niños (Modo Lectura Audible):**
  - **Pantalla de Bienvenida interactiva (`WelcomeScreen.kt`):** Guía visual y hablada con iconos explicativos de cada herramienta antes de entrar al lienzo.
  - Motor Text-To-Speech en español que narra en voz alta cada herramienta seleccionada, cambios de color, nombres de capas, sellos y acciones del lienzo.
  - Pensado especialmente para niños pequeños que aún no saben leer.

- **🥞 Sistema Multi-Capa Profesional (Layers):**
  - Crear, duplicar, reordenar y eliminar capas.
  - Control de opacidad por capa y bloqueo de visibilidad/edición.

- **✂️ Herramienta de Selección & Transformación:**
  - Selección de lazo (Lasso Tool) para recortar, mover, duplicar o eliminar trazos de forma independiente.

- **🎨 Páginas para Colorear (Plantillas / Stencils):**
  - Plantillas predefinidas: Dinosaurio, Cohete Espacial, Castillo Mágico, Unicornio, Océano y Flor.

- **🏰 Galería Local Persistente (Room DB & Bitmaps):**
  - Guardado local de obras de arte, miniaturas generadas dinámicamente y soporte para reabrir y continuar editando dibujos guardados.

- **⚙️ Integración CI/CD GitHub Actions:**
  - Pipeline automatizado (`build-apk.yml`) con caché Gradle y generación de firma en caliente (`debug.keystore`) para compilar y exportar el APK sin exponer claves privadas.

---

## 🛠 Stack Tecnológico Híbrido (Kotlin + C++ + Rust)

- **UI & Estado:** Jetpack Compose, Material 3, ViewModel, StateFlow.
- **Accesibilidad & Audio:** TextToSpeech Engine (`VoiceAssistant.kt`).
- **Persistencia:** Room Database, KSP, Kotlinx Serialization.
- **Motor C++ Native (JNI):** C++/SIMD para cálculo de curvas suavizadas de Chaikin y manipulación rápida de búferes de píxeles.
- **Motor Rust Core:** Módulo de seguridad en memoria Rust para simplificación de trazos vectoriales con el algoritmo Ramer-Douglas-Peucker.

---

## 🚀 Cómo Probar el Proyecto

1. Abrir el proyecto en **Android Studio**.
2. Sincronizar Gradle (`Sync Project with Gradle Files`).
3. Ejecutar en emulador o dispositivo físico con Android 8.0+ (API 26+).
4. Disfrutar de la experiencia de dibujo de alto rendimiento.

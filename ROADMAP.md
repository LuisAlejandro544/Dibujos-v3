# 🗺️ Roadmap de Desarrollo — KidsDraw Canvas

Este documento detalla la hoja de ruta de evolución para KidsDraw Canvas, dividida por fases estratégicas de desarrollo e integración técnica.

---

## 📌 Fase 1: Fundamentos & UI Interactiva (Completado ✅)
- [x] Implementación de lienzo interactivo de dibujo con Jetpack Compose Canvas.
- [x] Herramientas fundamentales: Lápiz, Pincel, Pincel Doble, Crayón, Neón, Borrador, Balde de Pintura y Sellos.
- [x] **Pinceles con Texturas & Efectos Especiales:** Estrellitas Mágicas ⭐, Burbujas Translúcidas 🫧, Galaxia Cósmica 🌌.
- [x] **Stickers Divertidos & Ojitos Saltones:** Ojitos Saltones 👀, Ojito Saltón 👁️, Gatito 🐱, Corona Real 👑, Varita Mágica 🪄, Lazo Coquetón 🎀, Gafas de Sol 🕶️.
- [x] **Paleta de Colores Expandida:** Paletas Neón, Pastel, Fuego, Metálicos y Mezclador Mágico con sliders RGB.
- [x] **Modo Asistente de Voz para Niños (Text-To-Speech):** Lectura hablada automática de botones, capas y herramientas en español.
- [x] Barra de herramientas colorida y paleta de colores dinámicas.
- [x] Sistema de capas con visibilidad, bloqueo, reordenamiento y opacidad.
- [x] Herramienta de Selección Lazo con transformación, duplicación y borrado.
- [x] Persistencia local con Room Database y exportación de miniaturas Bitmap.
- [x] Galería de arte para gestionar y reabrir proyectos.
- [x] Pipeline CI/CD GitHub Actions para compilación y firmado automático en caliente de APK Debug.

---

## 🚀 Fase 2: Integración Nativa Híbrida C++ & Rust & Rendimiento (Completado ✅)
- [x] Creación del puente JNI `NativeDrawingEngine.kt` con fallback seguro en Kotlin.
- [x] Módulo C++ (`drawing_engine.cpp`) con algoritmo de suavizado Chaikin SIMD y pipeline de verificación de GPU.
- [x] Módulo Rust (`lib.rs`) con simplificación Ramer-Douglas-Peucker para optimización vectorial.
- [x] **Aceleración por Hardware GPU (Compose & C++):** Renderizado acelerado de capas compuestas, baja latencia táctil y estado conmutable.
- [x] **Modo Horizontal Adaptativo (Landscape Quick Tools Rail):** Acceso ultra-rápido en 1 toque a herramientas, presets de grosor y colores en orientación horizontal.
- [x] **Modo Duelo de Dibujo 1vs1 Local con Juez Mágico AI:** Juego por turnos temporizado para 2 niños en el mismo dispositivo con prompts creativos generados por Gemini, evaluación visual multimodal en alta velocidad/calidad (`gemini-2.5-flash`), sistema de fallback automático y veredicto hablado.
- [x] **Gestor Avanzado de Capas:** Soporte para renombrar capas, combinar capas hacia abajo (Merge Down), limpiar contenido de capa, miniaturas en tiempo real (Live Thumbnails) y aviso audible si la capa está bloqueada.
- [ ] Compilación cruzada NDK automatizada para arquitecturas `arm64-v8a`, `armeabi-v7a` y `x86_64`.

---

## 🎨 Fase 3: Funciones Avanzadas de Arte & Asistencia IA (Próximamente 🔮)
- [ ] **Asistente IA de Bocetos:** Sugerencias automáticas de figuras a partir de trazos iniciales usando modelos Gemini en servidor.
- [ ] **Animación Cuadro a Cuadro:** Creación de GIFs y clips animados infantiles usando el sistema de capas como fotogramas.
- [ ] **Exportación Vectorial (SVG):** Conversión de trazos simplificados por Rust a formato vectorial SVG imprimible.
- [ ] **Sincronización Cloud Nube:** Respaldo opcional en Firebase Storage para guardar obras en la cuenta de usuario.

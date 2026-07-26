# 📂 Arquitectura y Estructura del Proyecto — KidsDraw

El proyecto está organizado bajo el patrón MVVM (Model-View-ViewModel) con Clean Architecture limpia, desarrollo modular en Jetpack Compose y módulos nativos JNI.

---

## 🌳 Árbol de Archivos Principal

```
/
├── .github/
│   └── workflows/
│       └── build-apk.yml                         # Pipeline GitHub Actions (Build & Sign APK)
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt               # Entry point conciso de la app & navegación de pantallas
│   │   │   │   ├── data/
│   │   │   │   │   ├── db/
│   │   │   │   │   │   ├── AppDatabase.kt        # Room Database singleton
│   │   │   │   │   │   ├── DrawingDao.kt         # Data Access Object para dibujos
│   │   │   │   │   │   └── DrawingEntity.kt      # Entidad de dibujo persistente
│   │   │   │   │   ├── models/
│   │   │   │   │   │   ├── DrawingLayer.kt       # Modelo de capa
│   │   │   │   │   │   ├── DrawingPath.kt        # Modelo de trazo vectorial
│   │   │   │   │   │   ├── ToolType.kt           # Enum de herramientas (Pincel, Neón, DUAL_BRUSH, etc.)
│   │   │   │   │   │   └── StencilType.kt        # Enum de plantillas para colorear
│   │   │   │   │   └── remote/
│   │   │   │   │       └── AiJudgeService.kt     # Integración Gemini REST API (Prompts & Evaluación 1v1)
│   │   │   │   ├── nativeengine/
│   │   │   │   │   └── NativeDrawingEngine.kt   # Puente JNI (Kotlin <-> C++ / Rust)
│   │   │   │   ├── ui/
   │   │   │   │   ├── canvas/
   │   │   │   │   │   ├── CanvasScreen.kt       # Vista composable principal de la pantalla de dibujo y modales
   │   │   │   │   │   ├── CanvasState.kt        # Estado reactivo del lienzo (CanvasUiState)
   │   │   │   │   │   ├── CanvasViewModel.kt    # ViewModel de estado, Undo/Redo & Duelo 1v1
   │   │   │   │   │   ├── DrawingCanvas.kt      # Composable interactivo viewport del lienzo
   │   │   │   │   │   ├── CanvasPathRenderer.kt # Módulo de renderizado de trazos (Pinceles, Neón, Borrador, Balde)
   │   │   │   │   │   ├── CanvasStampRenderer.kt# Módulo de renderizado de sellos (Estrella, Corazón, Sol, etc.)
   │   │   │   │   │   ├── CanvasSelectionOverlay.kt # Módulo de renderizado de caja de selección y tiradores
   │   │   │   │   │   ├── DrawingPersistenceManager.kt # Gestor de serialización JSON Moshi y persistencia Room
   │   │   │   │   │   └── Stencils.kt           # Plantillas vectoriales para colorear
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── BrushSettingsDrawer.kt
│   │   │   │   │   │   ├── ClearConfirmModal.kt  # Modal de confirmación para borrar capa o lienzo
│   │   │   │   │   │   ├── ColorPaletteBar.kt
│   │   │   │   │   │   ├── ColorPickerModal.kt   # Rueda de selección de color personalizada
│   │   │   │   │   │   ├── DuelHeaderBar.kt      # Barra de cabecera con temporizador para duelo 1vs1
│   │   │   │   │   │   ├── DuelResultModal.kt    # Modal con comparación de dibujos side-by-side y veredicto AI
│   │   │   │   │   │   ├── GalleryScreen.kt
│   │   │   │   │   │   ├── HelpGuideModal.kt     # Guía interactiva de uso
│   │   │   │   │   │   ├── LandscapeQuickToolsRail.kt # Riel de acceso rápido a herramientas en horizontal
│   │   │   │   │   │   ├── LayerManagerSheet.kt  # Gestión multi-capa
│   │   │   │   │   │   ├── MainMenuScreen.kt     # Pantalla de menú principal colorida
│   │   │   │   │   │   ├── SaveSuccessModal.kt   # Modal de celebración al guardar obra
│   │   │   │   │   │   ├── SelectionActionOverlay.kt # Acciones sobre selección (Duplicar, Borrar, Aplicar)
│   │   │   │   │   │   ├── StencilPickerModal.kt # Selector de libro para colorear
│   │   │   │   │   │   ├── ToolBar.kt
│   │   │   │   │   │   ├── TopBarControls.kt
│   │   │   │   │   │   └── WelcomeScreen.kt      # Pantalla de introducción y guía de herramientas
│   │   │   │   │   └── theme/
│   │   │   │   │       ├── Color.kt              # Paleta M3 para niños
│   │   │   │   │       ├── Theme.kt
│   │   │   │   │       └── Type.kt
│   │   │   │   └── utils/
│   │   │   │       ├── DrawingUtils.kt           # Renderizador Bitmap, colores complementarios & matemática
│   │   │   │       └── VoiceAssistant.kt         # Motor Text-To-Speech para niños
│   │   │   ├── cpp/
│   │   │   │   ├── CMakeLists.txt                # Configuración C++ CMake
│   │   │   │   └── drawing_engine.cpp            # Subsistema C++ SIMD
│   │   │   └── rust/
│   │   │       ├── Cargo.toml                    # Manifest Rust
│   │   │       └── src/
│   │   │           └── lib.rs                    # Núcleo Rust RDP Vector Simplify
│   ├── skills/                                   # Habilidades personalizadas de IA
│   │   └── system_skills/
│   │       └── ai_drawing_judge/
│   │           └── SKILL.md                      # Prompting & Skill del Juez Mágico AI
├── metadata.json                                 # Metadata para AI Studio
├── README.md                                     # Guía general del proyecto
├── ROADMAP.md                                    # Hoja de ruta de características
├── STRUCTURE.md                                  # Mapa de arquitectura y archivos
├── AI_CONTEXT.md                                 # Manual de contexto para modelos AI
└── AGENTS.md                                     # Reglas de conducta para agentes AI
```

---

## 🔄 Flujo de Datos Modular

1. **Navegación:** `MainActivity.kt` gestiona el estado de pantalla global (`AppScreen`) y delega la vista del lienzo a `CanvasScreen.kt`.
2. **Gestión de Gestos:** `DrawingCanvas.kt` captura eventos táctiles (`PointerInput`) y los envía a `CanvasViewModel.kt`.
3. **Renderizado Separado:** `DrawingCanvas.kt` utiliza los sub-renderizadores especializados `CanvasPathRenderer.kt`, `CanvasStampRenderer.kt` y `CanvasSelectionOverlay.kt` para dibujar trazos, sellos y overlays en el `DrawScope`.
4. **Persistencia Desacoplada:** `DrawingPersistenceManager.kt` maneja de forma asíncrona la serialización JSON con Moshi y el guardado/recuperación en la base de datos Room.
5. **Evaluación 1vs1 AI:** `AiJudgeService.kt` solicita temas creativos e invoca la API de Gemini para la evaluación multimodal de los dibujos del Duelo 1v1.

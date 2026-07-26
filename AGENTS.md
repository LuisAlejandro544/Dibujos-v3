# 🤖 Reglas de Conducta para Agentes AI (AGENTS.md)

Este documento especifica los principios y normas de comportamiento que todos los agentes de desarrollo automático deben cumplir estrictamente al modificar este proyecto.

---

## 📜 Principios Fundamentales

1. **Respeto Absoluto a la Intención del Usuario:**
   - Construye exactamente lo solicitado. No agregues características invasivas no pedidas que aumenten la complejidad sin justificación.

2. **Calidad de Código, Estilo & Accesibilidad:**
   - Escribe código limpio, legible y modular en Kotlin y Jetpack Compose.
   - Sigue el diseño M3 con colores alegres, accesibles y redondeados orientados a niños.
   - Preserva la integración con `VoiceAssistant.kt` (Text-To-Speech) para que los niños que no saben leer escuchen qué acción o herramienta seleccionan.
   - Incluye `contentDescription` en todos los composables interactivos.
   - Asegura un área táctil mínima de 48dp en botones y elementos interactivos.

3. **Verificación Sistemática del Proyecto:**
   - Tras realizar cambios en el código, ejecuta siempre `compile_applet` para confirmar que el proyecto compila sin errores.
   - Si la compilación falla, analiza los logs de error e implementa correcciones dirigidas.

4. **Preservación de la Estructura Nativa Híbrida & CI/CD:**
   - Respeta los archivos de C++ (`/app/src/main/cpp/`) y Rust (`/app/src/main/rust/`).
   - Manten el puente JNI en `NativeDrawingEngine.kt` con sus métodos de respaldo (fallbacks) en Kotlin.
   - Preserva la integración de integración continua en `.github/workflows/build-apk.yml`.

5. **Comunicación Profesional y Directa:**
   - Mantén explicaciones claras, objetivas y orientadas al usuario final.
   - Evita jerga técnica excesiva en los resúmenes finales a menos que sea explícitamente solicitada.

# ☁️ Cloudflare Worker Proxy para Gemini AI (Gratis y Seguro)

Para solucionar el error de compilación en Cloudflare o conectar tu proyecto directamente desde GitHub:

 He incluido en el repositorio la carpeta independiente `/cloudflare-worker/` lista para desplegar.

---

## 📂 Archivos creados en el proyecto

- `/cloudflare-worker/index.js` -> Código del proxy JavaScript estándar en ES Modules (0 dependencias, no requiere build).
- `/cloudflare-worker/wrangler.toml` -> Configuración para desplegar con Wrangler o integración con GitHub.
- `/cloudflare-worker/package.json` -> Script de despliegue directo (`npm run deploy`).

---

## 🐙 OPCIÓN 1: Conectar tu Repositorio de GitHub directamente a Cloudflare (Recomendado)

¡Sí, exactamente! Puedes vincular tu repositorio de GitHub y seleccionar la carpeta `/cloudflare-worker`:

1. En el Dashboard de Cloudflare -> **Workers & Pages** -> **Create Application**.
2. Selecciona la pestaña **Pages** o **Workers** y haz clic en **Connect to Git** (Conectar a Git).
3. Selecciona tu repositorio de GitHub importado.
4. Configura los siguientes campos:
   - **Root directory / Directorio raíz**: `cloudflare-worker`
   - **Framework preset**: `None` / `Unspecified`
   - **Build command / Comando de construcción**: *(Déjalo completamente vacío)*
   - **Build output directory / Directorio de salida**: `.` *(o déjalo vacío)*
5. En la sección **Environment variables (Variables de entorno)**:
   - Nombre: `GEMINI_API_KEY`
   - Valor: *(Tu API key de Gemini de Google AI Studio)*
6. Haz clic en **Save and Deploy**. 

Cloudflare desplegará automáticamente el Worker leyendo el archivo `index.js` de la carpeta `cloudflare-worker` cada vez que hagas un push a tu GitHub.

---

## 🚀 OPCIÓN 2: Copiar y Pegar en el Editor Web (2 minutos)

1. En el Dashboard de Cloudflare -> **Workers & Pages** -> **Create Application**.
2. Selecciona **Create Worker**.
3. Elige la plantilla **Hello World (JavaScript)** (*NO selecciones TypeScript*).
4. Asigna un nombre al worker (ejemplo: `kidsdraw-gemini-proxy`) y dale a **Deploy**.
5. Haz clic en **Edit Code** (Editar código).
6. Copia el contenido de `/cloudflare-worker/index.js` de este repositorio, pégalo reemplazando todo y dale a **Save and Deploy**.
7. Ve a **Settings** -> **Variables and Secrets**:
   - Nombre: `GEMINI_API_KEY`
   - Valor: *(Tu API key de Gemini)*

---

## ⚡ OPCIÓN 3: Despliegue con Comando (Wrangler CLI)

```bash
cd cloudflare-worker
npx wrangler login
npx wrangler secret put GEMINI_API_KEY
npx wrangler deploy
```

---

## 📱 Configurar la URL en la App Android

Copia la URL del worker que te da Cloudflare (ejemplo: `https://kidsdraw-gemini-proxy.tu-subdominio.workers.dev`) y colócala en tu `.env` o variable de compilación:

```env
GEMINI_WORKER_URL=https://kidsdraw-gemini-proxy.tu-subdominio.workers.dev
```


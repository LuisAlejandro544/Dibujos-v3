# 🤖 Guía de Configuración: Actualizador de Código por Telegram (.ZIP)

Esta solución te permite enviar un archivo `.zip` con tu código actualizado a un **Bot de Telegram**, y automáticamente:
1. Descomprime el código fuente.
2. Reemplaza el código en tu repositorio GitHub directamente en la rama `main` (sin crear nuevas ramas ni Pull Requests).
3. Hace commit y push de manera transparente.
4. Inicia la compilación del APK automáticamente.
5. Te envía un mensaje a Telegram cuando la actualización termina con éxito.

---

## 🔑 1. Configuración de Secrets en GitHub

Ve a tu repositorio en GitHub: **Settings -> Secrets and variables -> Actions -> New repository secret**.

Agrega los siguientes **3 Secrets**:

| Nombre del Secret | Descripción |
| :--- | :--- |
| `GH_PAT` | **Personal Access Token (PAT)** con permiso de escritura en repositorios (`repo` scope). Se usa para permitir que el workflow haga `git push` directo a `main`. |
| `TELEGRAM_BOT_TOKEN` | El token de tu Bot obtenido desde [@BotFather](https://t.me/BotFather) en Telegram. |
| `TELEGRAM_CHAT_ID` | (Opcional) Tu ID numérico de usuario en Telegram para recibir notificaciones directas. |

---

## 🛠️ 2. Cómo Obtener el Personal Access Token (`GH_PAT`)
1. Ve a GitHub: **Settings de tu cuenta personal -> Developer Settings -> Personal access tokens -> Tokens (classic)**.
2. Haz clic en **Generate new token (classic)**.
3. Ponle de nombre `Telegram Bot Auto Push`.
4. Marca la casilla **`repo`** (Full control of private repositories).
5. Haz clic en **Generate token** y copia el token generado (empieza con `ghp_...`).
6. Pégalo en los **Secrets de GitHub** con el nombre `GH_PAT`.

---

## 🤖 3. Crear el Bot de Telegram
1. En Telegram, busca a [@BotFather](https://t.me/BotFather).
2. Envía el comando `/newbot`.
3. Asígnale un nombre (ej. `Mi App Auto Deploy Bot`) y un usuario terminado en `bot`.
4. Guarda el **API Token** que te entregue BotFather (ej. `123456789:ABCdefGHI...`).

---

## ☁️ 4. Desplegar el Bot (en Cloudflare Workers o con Webhook)

El código del servidor del bot está disponible en `/telegram-bot/worker.js`.

### Pasos para desplegar en Cloudflare Workers (Gratis):
1. Entra a tu panel de **Cloudflare -> Workers & Pages -> Create Application -> Create Worker**.
2. Pega el código que está en el archivo `/telegram-bot/worker.js`.
3. Ve a **Settings -> Variables** del Worker y agrega las siguientes variables de entorno:
   - `TELEGRAM_BOT_TOKEN`: El token de tu bot de @BotFather.
   - `GH_PAT`: Tu GitHub Personal Access Token (`ghp_...`).
   - `GH_REPO`: Tu usuario y repositorio de GitHub (ejemplo: `mi-usuario/mi-repositorio-android`).
4. Copia la URL de tu Worker (ejemplo: `https://mi-telegram-bot.midominio.workers.dev`).
5. Registra el Webhook en Telegram abriendo esta URL en tu navegador:
   `https://api.telegram.org/bot<TU_TELEGRAM_BOT_TOKEN>/setWebhook?url=https://mi-telegram-bot.midominio.workers.dev`

---

## 🚀 5. Modo de Uso
1. Abre tu bot en Telegram.
2. Envíale un archivo `.zip` con el código actualizado de tu proyecto.
3. El bot te responderá confirmando que recibió el archivo.
4. En cuestión de segundos, la rama `main` en GitHub se actualizará automáticamente y te llegará una notificación de confirmación.

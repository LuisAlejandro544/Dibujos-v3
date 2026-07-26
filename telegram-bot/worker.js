// Cloudflare Worker para Bot de Telegram -> GitHub Repository Dispatch
export default {
  async fetch(request, env, ctx) {
    if (request.method !== "POST") {
      return new Response("Bot de Telegram activo y listo.", { status: 200 });
    }

    try {
      const update = await request.json();
      const message = update.message;

      if (!message) {
        return new Response("OK", { status: 200 });
      }

      const chatId = message.chat.id;
      const botToken = env.TELEGRAM_BOT_TOKEN;
      const ghPat = env.GH_PAT;
      const ghRepo = env.GH_REPO; // Formato: "tu-usuario/tu-repositorio"

      if (!botToken || !ghPat || !ghRepo) {
        if (botToken) {
          await sendTelegramMessage(
            botToken,
            chatId,
            "❌ *Error de configuración:* Las variables `TELEGRAM_BOT_TOKEN`, `GH_PAT` o `GH_REPO` no están totalmente configuradas en Cloudflare Workers."
          );
        }
        return new Response("OK", { status: 200 });
      }

      // Responder al comando /start
      if (message.text && message.text.startsWith("/start")) {
        await sendTelegramMessage(
          botToken,
          chatId,
          "👋 *¡Hola! Soy tu bot actualizador de código en GitHub.*\n\nEnvíame un archivo **.ZIP** con el código fuente de tu proyecto y actualizaré la rama `main` automáticamente en GitHub sin crear ramas adicionales."
        );
        return new Response("OK", { status: 200 });
      }

      // Verificar si el mensaje contiene un archivo adjunto .ZIP
      if (message.document) {
        const fileName = message.document.file_name || "codigo.zip";
        const fileId = message.document.file_id;
        const fileSize = message.document.file_size ? `${(message.document.file_size / 1024).toFixed(1)} KB` : "";
        const mimeType = message.document.mime_type || "";

        const isZip = fileName.toLowerCase().endsWith(".zip") ||
                      mimeType.includes("zip") ||
                      mimeType.includes("compressed") ||
                      mimeType.includes("octet-stream");

        if (!isZip) {
          await sendTelegramMessage(
            botToken,
            chatId,
            `❌ *Archivo no procesado:* \`${fileName}\`\n⚠️ Por favor envía exclusivamente un archivo comprimido con extensión **.ZIP**.`
          );
          return new Response("OK", { status: 200 });
        }

        // Notificar recepción inmediata del archivo ZIP
        await sendTelegramMessage(
          botToken,
          chatId,
          `📦 *¡Archivo ZIP recibido con éxito!*\n📄 **Nombre:** \`${fileName}\` ${fileSize ? `(${fileSize})` : ""}\n⏳ Procesando descarga e iniciando actualización en GitHub...`
        );

        // 1. Obtener la ruta de descarga del archivo desde Telegram
        const getFileRes = await fetch(
          `https://api.telegram.org/bot${botToken}/getFile?file_id=${fileId}`
        );
        const getFileData = await getFileRes.json();

        if (!getFileData.ok || !getFileData.result.file_path) {
          await sendTelegramMessage(
            botToken,
            chatId,
            "❌ *Error al procesar:* No se pudo obtener la ruta de descarga del archivo desde Telegram."
          );
          return new Response("OK", { status: 200 });
        }

        const filePath = getFileData.result.file_path;
        const zipUrl = `https://api.telegram.org/file/bot${botToken}/${filePath}`;

        // 2. Disparar evento repository_dispatch en GitHub
        const ghResponse = await fetch(
          `https://api.github.com/repos/${ghRepo}/dispatches`,
          {
            method: "POST",
            headers: {
              Authorization: `Bearer ${ghPat}`,
              Accept: "application/vnd.github.v3+json",
              "User-Agent": "Telegram-GitHub-Sync-Bot",
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              event_type: "update_code_zip",
              client_payload: {
                zip_url: zipUrl,
                chat_id: chatId,
              },
            }),
          }
        );

        if (ghResponse.ok || ghResponse.status === 204) {
          await sendTelegramMessage(
            botToken,
            chatId,
            "🚀 *Acción enviada a GitHub.* La actualización de la rama `main` y la compilación del APK han comenzado. Te notificaré automáticamente al finalizar."
          );
        } else {
          const errText = await ghResponse.text();
          await sendTelegramMessage(
            botToken,
            chatId,
            `❌ *Error al comunicarse con la API de GitHub* (${ghResponse.status}): ${errText}`
          );
        }
      }

      return new Response("OK", { status: 200 });
    } catch (err) {
      return new Response(`Error: ${err.message}`, { status: 500 });
    }
  },
};

async function sendTelegramMessage(botToken, chatId, text) {
  return fetch(`https://api.telegram.org/bot${botToken}/sendMessage`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      chat_id: chatId,
      text: text,
      parse_mode: "Markdown",
    }),
  });
}

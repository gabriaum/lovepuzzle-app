package com.gabriaum.app.service

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class WebhookService {
    fun sendDiscordWebhook(webhookUrl: String, message: String) {
        if (webhookUrl.isEmpty()) return

        Thread {
            try {
                val client = OkHttpClient()
                val json = JSONObject()
                json.put("content", message)
                val body = json.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(webhookUrl)
                    .post(body)
                    .build()

                client.newCall(request).execute()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
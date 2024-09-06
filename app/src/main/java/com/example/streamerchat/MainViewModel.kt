package com.example.streamerchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.OkHttpClient
import java.io.IOException

class StreamViewModel : ViewModel() {

    private val _responseLiveData = MutableLiveData<String>()
    val responseLiveData: LiveData<String> get() = _responseLiveData

    private val client = OkHttpClient()

    // Fungsi untuk memulai streaming dari server
    fun streamResponse(prompt: String) {
        val url = "https://ms/chat"
        val json = """{"prompt": "$prompt"}"""

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), json
        )

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                _responseLiveData.postValue("Error: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body

                    // Baca respons bertahap
                    responseBody?.source()?.let { source ->
                        val buffer = StringBuilder()
                        while (!source.exhausted()) {
                            val line = source.readUtf8Line()
                            if (line != null) {
                                for (char in line) {
                                    buffer.append(char)
                                    // Update LiveData setiap karakter baru diterima
                                    //_responseLiveData.postValue(buffer.toString())
                                    _responseLiveData.postValue(formatText(buffer.toString()))
                                    // Simulasi delay (opsional) untuk melihat efek streaming per karakter
                                    Thread.sleep(50)
                                }
                            }
                        }
                    }
                } else {
                    _responseLiveData.postValue("Response failed")
                }
            }
        })
    }

    // Fungsi untuk memformat teks
    private fun formatText(text: String): String {
        // Format teks jika perlu
        // Contoh: menambahkan spasi setelah setiap titik
        return text.replace("(?<!\\S)\\.\\s".toRegex(), ".\n\n")
    }
}


/*
class StreamViewModel : ViewModel() {

    private val _responseLiveData = MutableLiveData<String>()
    val responseLiveData: LiveData<String> get() = _responseLiveData

    private val client = OkHttpClient()

    // Fungsi untuk memulai streaming dari server
    fun streamResponse(prompt: String) {
        val url = "https://.ms/chat"
        val json = """{"prompt": "$prompt"}"""

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), json
        )

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                _responseLiveData.postValue("Error: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body

                    // Baca respons bertahap
                    responseBody?.source()?.let { source ->
                        val buffer = StringBuilder()
                        val handler = Handler(Looper.getMainLooper()) // Untuk update UI di thread utama
                        while (!source.exhausted()) {
                            val line = source.readUtf8Line()
                            if (line != null) {
                                handler.post {
                                    buffer.append(line).append("\n") // Tambahkan newline untuk format rapi
                                    _responseLiveData.value = buffer.toString()
                                }
                                // Simulasi delay
                                Thread.sleep(50)
                            }
                        }
                    }
                } else {
                    _responseLiveData.postValue("Response failed")
                }
            }
        })
    }
}
 */


/*class StreamViewModel : ViewModel() {

    private val _responseLiveData = MutableLiveData<String>()
    val responseLiveData: LiveData<String> get() = _responseLiveData

    private val client = OkHttpClient()

    // Fungsi untuk memulai streaming dari server menggunakan POST
    fun streamResponse(prompt: String) {
        val url = "https://s.ms/chat"
        val json = """{"prompt": "$prompt"}"""

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(), json
        )

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                _responseLiveData.postValue("Error: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body
                    // Baca respons bertahap
                    responseBody?.source()?.let { source ->
                        val buffer = StringBuilder()
                        while (!source.exhausted()) {
                            val line = source.readUtf8Line()
                            Log.d("CEK", line.toString())
                            if (line != null) {
                                buffer.append(line).append("\n")
                                // Update LiveData setiap ada message baru
                                _responseLiveData.postValue(buffer.toString())
                            }
                        }
                    }
                } else {
                    _responseLiveData.postValue("Response failed")
                }
            }
        })
    }
}

 */

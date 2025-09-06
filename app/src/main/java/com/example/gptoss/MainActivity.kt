package com.example.gptoss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gptoss.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<Message>()
    private val client = OkHttpClient()
    private val gson = Gson()

    private val apiUrl = "https://amd-gpt-oss-120b-chatbot.hf.space/run/chat"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.buttonSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messageList)
        binding.recyclerViewChat.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = chatAdapter
        }
    }

    private fun sendMessage() {
        val messageText = binding.editTextMessage.text.toString().trim()
        if (messageText.isNotEmpty()) {
            // Add user message to UI
            val userMessage = Message(messageText, Message.VIEW_TYPE_USER)
            runOnUiThread {
                chatAdapter.addMessage(userMessage)
                binding.recyclerViewChat.scrollToPosition(chatAdapter.itemCount - 1)
            }
            binding.editTextMessage.setText("")

            // Send message to API
            postRequest(messageText)
        }
    }

    private fun postRequest(messageText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val requestPayload = mapOf(
                    "data" to listOf(
                        messageText,
                        "You are a helpful assistant.",
                        0.7,
                        "low",
                        false
                    )
                )
                val body = gson.toJson(requestPayload)
                    .toRequestBody("application/json; charset=utf-f".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseBody = response.body?.string()
                    val apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)
                    val aiMessageText = apiResponse.data.firstOrNull() ?: "Sorry, I couldn't get a response."

                    withContext(Dispatchers.Main) {
                        val aiMessage = Message(aiMessageText, Message.VIEW_TYPE_AI)
                        chatAdapter.addMessage(aiMessage)
                        binding.recyclerViewChat.scrollToPosition(chatAdapter.itemCount - 1)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    val errorMessage = Message("Error: ${e.message}", Message.VIEW_TYPE_AI)
                    chatAdapter.addMessage(errorMessage)
                    binding.recyclerViewChat.scrollToPosition(chatAdapter.itemCount - 1)
                }
            }
        }
    }

    // Data class to parse the JSON response from the API
    data class ApiResponse(val data: List<String>)
}

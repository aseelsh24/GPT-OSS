<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" style="height:64px;margin-right:32px"/>

# 🎯 مشروع GPT-OSS Chat - جميع الملفات

تم إنجاز المشروع بنجاح! إليك جميع الملفات المطلوبة لتطبيق Android GPT-OSS Chat:

## 📁 هيكل المشروع الكامل

```
GPT-OSS/
├── .github/workflows/build-and-release.yml
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/gptoss/chat/
│       │   ├── MainActivity.kt
│       │   ├── models/Message.kt
│       │   ├── utils/
│       │   │   ├── MessageType.kt
│       │   │   └── MessageUtils.kt
│       │   ├── adapters/MessageAdapter.kt
│       │   └── api/
│       │       ├── GradioApiService.kt
│       │       ├── ApiClient.kt
│       │       └── ApiManager.kt
│       └── res/
│           ├── layout/
│           │   ├── activity_main.xml
│           │   └── item_message.xml
│           ├── values/
│           │   ├── strings.xml
│           │   ├── colors.xml
│           │   ├── themes.xml
│           │   └── dimens.xml
│           └── drawable/
│               ├── message_background_user.xml
│               └── message_background_bot.xml
├── build.gradle.kts (Project)
├── settings.gradle.kts
├── gradle.properties
├── gradle/wrapper/gradle-wrapper.properties
└── README.md
```


***

## 🏗️ 1. ملفات التكوين الأساسية

### build.gradle.kts (Project Level)

```kotlin
plugins {
    id("com.android.application") version "8.7.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}
```


### settings.gradle.kts

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GPT-OSS"
include(":app")
```


### gradle.properties

```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.enableJetifier=true
kotlin.code.style=official
android.nonTransitiveRClass=true
```


### app/build.gradle.kts

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.gptoss.chat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gptoss.chat"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    
    // JSON
    implementation("com.google.code.gson:gson:2.10.1")
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```


***

## 📱 2. Android Manifest

### app/src/main/AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.GPTOSS"
        android:usesCleartextTraffic="true"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.GPTOSS">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```


***

## 🎨 3. ملفات التصميم والواجهة

### app/src/main/res/layout/activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/background_color">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewMessages"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:paddingHorizontal="8dp"
        android:paddingTop="8dp"
        android:clipToPadding="false"
        android:scrollbars="vertical"
        android:fadeScrollbars="true" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="@dimen/input_padding"
        android:background="@color/input_background"
        android:elevation="4dp">

        <EditText
            android:id="@+id/editTextMessage"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:hint="@string/type_message"
            android:background="@drawable/message_background_user"
            android:padding="12dp"
            android:maxLines="4"
            android:textColor="@color/text_color"
            android:textColorHint="@color/hint_color"
            android:inputType="textMultiLine|textCapSentences"
            android:imeOptions="actionSend" />

        <com.google.android.material.button.MaterialButton
            android:id="@+id/buttonSend"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:text="@string/send"
            android:backgroundTint="@color/primary_color"
            app:cornerRadius="24dp"
            android:minWidth="72dp" />

    </LinearLayout>

</LinearLayout>
```


### app/src/main/res/layout/item_message.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="4dp">

    <LinearLayout
        android:id="@+id/messageContainer"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_gravity="end"
        android:background="@drawable/message_background_user"
        android:padding="12dp"
        android:layout_marginVertical="2dp"
        android:maxWidth="280dp"
        android:minWidth="64dp"
        android:elevation="2dp">

        <TextView
            android:id="@+id/textViewMessage"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="@color/message_text_color"
            android:textSize="16sp"
            android:lineSpacingExtra="2dp"
            android:textIsSelectable="true"
            android:autoLink="web|email|phone"
            android:linksClickable="true" />

        <TextView
            android:id="@+id/textViewTime"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="end"
            android:textSize="11sp"
            android:textColor="@color/time_text_color"
            android:layout_marginTop="4dp" />

    </LinearLayout>

</LinearLayout>
```


***

## 🎨 4. ملفات الموارد

### app/src/main/res/values/strings.xml

```xml
<resources>
    <string name="app_name">GPT-OSS Chat</string>
    <string name="type_message">Type your message...</string>
    <string name="send">Send</string>
    <string name="sending">Sending...</string>
    <string name="user">You</string>
    <string name="bot">GPT-OSS</string>
    <string name="error_network">Network error. Please check your connection.</string>
    <string name="error_api">API error. Please try again later.</string>
    <string name="welcome_message">Hello! I\'m GPT-OSS, your AI assistant. How can I help you today?</string>
    <string name="no_internet">No internet connection available</string>
    <string name="retry">Retry</string>
    <string name="connection_timeout">Connection timeout. Please try again.</string>
</resources>
```


### app/src/main/res/values/colors.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    
    <color name="primary_color">#FF2196F3</color>
    <color name="primary_dark">#FF1976D2</color>
    <color name="primary_light">#FFBBDEFB</color>
    
    <color name="background_color">#FFF5F5F5</color>
    <color name="input_background">#FFFFFFFF</color>
    <color name="surface_color">#FFFFFFFF</color>
    
    <color name="text_color">#FF212121</color>
    <color name="text_secondary">#FF757575</color>
    <color name="hint_color">#FF9E9E9E</color>
    
    <color name="message_text_color">#FFFFFFFF</color>
    <color name="time_text_color">#B3FFFFFF</color>
    <color name="user_message_bg">#FF2196F3</color>
    <color name="bot_message_bg">#FFE0E0E0</color>
    <color name="bot_message_text">#FF212121</color>
    
    <color name="success_color">#FF4CAF50</color>
    <color name="error_color">#FFF44336</color>
    <color name="warning_color">#FFFF9800</color>
</resources>
```


### app/src/main/res/values/themes.xml

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <style name="Theme.GPTOSS" parent="Theme.Material3.DayNight">
        <item name="colorPrimary">@color/primary_color</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_700</item>
        <item name="colorOnSecondary">@color/black</item>
        <item name="android:statusBarColor" tools:targetApi="l">?attr/colorPrimaryVariant</item>
    </style>
</resources>
```


### app/src/main/res/values/dimens.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <dimen name="message_margin_near">16dp</dimen>
    <dimen name="message_margin_far">64dp</dimen>
    <dimen name="message_padding">12dp</dimen>
    <dimen name="message_vertical_spacing">2dp</dimen>
    <dimen name="message_corner_radius">16dp</dimen>
    <dimen name="input_padding">16dp</dimen>
    <dimen name="input_margin">8dp</dimen>
    <dimen name="message_text_size">16sp</dimen>
    <dimen name="time_text_size">11sp</dimen>
    <dimen name="message_elevation">2dp</dimen>
    <dimen name="input_elevation">4dp</dimen>
</resources>
```


### app/src/main/res/drawable/message_background_user.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/user_message_bg" />
    <corners android:radius="16dp" />
</shape>
```


### app/src/main/res/drawable/message_background_bot.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/bot_message_bg" />
    <corners android:radius="16dp" />
</shape>
```


***

## 💻 5. ملفات Kotlin

### app/src/main/java/com/gptoss/chat/MainActivity.kt

```kotlin
package com.gptoss.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gptoss.chat.adapters.MessageAdapter
import com.gptoss.chat.api.ApiException
import com.gptoss.chat.api.ApiManager
import com.gptoss.chat.api.NetworkException
import com.gptoss.chat.databinding.ActivityMainBinding
import com.gptoss.chat.models.Message
import com.gptoss.chat.utils.MessageUtils
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()
    private lateinit var apiManager: ApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiManager = ApiManager.getInstance(this)
        setupRecyclerView()
        setupClickListeners()
        addWelcomeMessage()
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messages)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true

        binding.recyclerViewMessages.apply {
            this.layoutManager = layoutManager
            adapter = messageAdapter
            addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                if (bottom < oldBottom) {
                    scrollToPosition(messageAdapter.itemCount - 1)
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString().trim()
            if (MessageUtils.isValidMessage(messageText)) {
                sendMessage(MessageUtils.sanitizeMessage(messageText))
                binding.editTextMessage.text.clear()
            }
        }

        binding.editTextMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                binding.buttonSend.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun addWelcomeMessage() {
        val welcomeMessage = MessageUtils.createBotMessage(
            "Hello! I'm GPT-OSS, your AI assistant. How can I help you today?"
        )
        addMessageToChat(welcomeMessage)
    }

    private fun sendMessage(messageText: String) {
        val userMessage = MessageUtils.createUserMessage(messageText)
        addMessageToChat(userMessage)

        setLoadingState(true)

        lifecycleScope.launch {
            apiManager.sendChatMessage(messageText)
                .onSuccess { response ->
                    val botMessage = MessageUtils.createBotMessage(response)
                    addMessageToChat(botMessage)
                }
                .onFailure { exception ->
                    handleApiError(exception)
                }
            
            setLoadingState(false)
        }
    }

    private fun handleApiError(exception: Throwable) {
        val errorMessage = when (exception) {
            is NetworkException -> {
                Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_SHORT).show()
                "Sorry, I'm having trouble connecting to the server. Please check your internet connection and try again."
            }
            is ApiException -> {
                Toast.makeText(this, getString(R.string.error_api), Toast.LENGTH_SHORT).show()
                "Sorry, I encountered an API error (Code: ${exception.code}). Please try again in a moment."
            }
            else -> {
                Toast.makeText(this, "Unexpected error occurred", Toast.LENGTH_SHORT).show()
                "Sorry, something unexpected happened. Please try again."
            }
        }
        
        val botErrorMessage = MessageUtils.createBotMessage(errorMessage)
        addMessageToChat(botErrorMessage)
    }

    private fun addMessageToChat(message: Message) {
        messages.add(message)
        messageAdapter.notifyItemInserted(messages.size - 1)
        scrollToBottom()
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.buttonSend.isEnabled = !isLoading
        binding.buttonSend.text = if (isLoading) getString(R.string.sending) else getString(R.string.send)
        binding.editTextMessage.isEnabled = !isLoading
    }

    private fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            binding.recyclerViewMessages.post {
                binding.recyclerViewMessages.smoothScrollToPosition(messages.size - 1)
            }
        }
    }
}
```


### app/src/main/java/com/gptoss/chat/models/Message.kt

```kotlin
package com.gptoss.chat.models

import com.gptoss.chat.utils.MessageType

data class Message(
    val text: String,
    val type: MessageType,
    val timestamp: String
) {
    fun isFromUser(): Boolean = type == MessageType.USER
    fun isFromBot(): Boolean = type == MessageType.BOT
}
```


### app/src/main/java/com/gptoss/chat/utils/MessageType.kt

```kotlin
package com.gptoss.chat.utils

enum class MessageType {
    USER, 
    BOT;
    
    companion object {
        fun fromString(type: String): MessageType {
            return when (type.uppercase()) {
                "USER" -> USER
                "BOT" -> BOT
                else -> BOT
            }
        }
    }
}
```


### app/src/main/java/com/gptoss/chat/utils/MessageUtils.kt

```kotlin
package com.gptoss.chat.utils

import com.gptoss.chat.models.Message
import java.text.SimpleDateFormat
import java.util.*

object MessageUtils {
    
    fun createUserMessage(text: String): Message {
        return Message(
            text = text,
            type = MessageType.USER,
            timestamp = getCurrentTime()
        )
    }
    
    fun createBotMessage(text: String): Message {
        return Message(
            text = text,
            type = MessageType.BOT,
            timestamp = getCurrentTime()
        )
    }
    
    fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }
    
    fun formatTimestamp(timestamp: String): String {
        return try {
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(timestamp)
            SimpleDateFormat("h:mm a", Locale.getDefault()).format(time!!)
        } catch (e: Exception) {
            timestamp
        }
    }
    
    fun isValidMessage(text: String): Boolean {
        return text.isNotBlank() && text.trim().length > 0
    }
    
    fun sanitizeMessage(text: String): String {
        return text.trim().replace(Regex("\\s+"), " ")
    }
}
```


### app/src/main/java/com/gptoss/chat/adapters/MessageAdapter.kt

```kotlin
package com.gptoss.chat.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gptoss.chat.R
import com.gptoss.chat.databinding.ItemMessageBinding
import com.gptoss.chat.models.Message
import com.gptoss.chat.utils.MessageType

class MessageAdapter(private val messages: List<Message>) : 
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(private val binding: ItemMessageBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.textViewMessage.text = message.text
            binding.textViewTime.text = message.timestamp

            val context = binding.root.context
            val containerLayoutParams = binding.messageContainer.layoutParams as LinearLayout.LayoutParams

            when (message.type) {
                MessageType.USER -> {
                    binding.messageContainer.setBackgroundResource(R.drawable.message_background_user)
                    binding.textViewMessage.setTextColor(
                        ContextCompat.getColor(context, R.color.message_text_color)
                    )
                    binding.textViewTime.setTextColor(
                        ContextCompat.getColor(context, R.color.time_text_color)
                    )
                    
                    containerLayoutParams.gravity = Gravity.END
                    containerLayoutParams.setMargins(
                        context.resources.getDimensionPixelSize(R.dimen.message_margin_far),
                        0,
                        context.resources.getDimensionPixelSize(R.dimen.message_margin_near),
                        0
                    )
                }
                
                MessageType.BOT -> {
                    binding.messageContainer.setBackgroundResource(R.drawable.message_background_bot)
                    binding.textViewMessage.setTextColor(
                        ContextCompat.getColor(context, R.color.bot_message_text)
                    )
                    binding.textViewTime.setTextColor(
                        ContextCompat.getColor(context, R.color.bot_message_text)
                    )
                    
                    containerLayoutParams.gravity = Gravity.START
                    containerLayoutParams.setMargins(
                        context.resources.getDimensionPixelSize(R.dimen.message_margin_near),
                        0,
                        context.resources.getDimensionPixelSize(R.dimen.message_margin_far),
                        0
                    )
                }
            }
            
            binding.messageContainer.layoutParams = containerLayoutParams
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size
    
    fun addMessage(message: Message) {
        if (messages is MutableList) {
            messages.add(message)
            notifyItemInserted(messages.size - 1)
        }
    }
    
    fun getLastMessage(): Message? {
        return if (messages.isNotEmpty()) messages.last() else null
    }
}
```


***

## 🌐 6. ملفات API

### app/src/main/java/com/gptoss/chat/api/GradioApiService.kt

```kotlin
package com.gptoss.chat.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface GradioApiService {
    @Headers("Content-Type: application/json")
    @POST
    suspend fun sendMessage(
        @Url url: String,
        @Body request: GradioRequest
    ): Response<GradioResponse>
}

data class GradioRequest(
    val fn_index: Int = 0,
    val data: List<Any>,
    val session_hash: String = generateDefaultSession(),
    val event_data: Any? = null
) {
    companion object {
        fun generateDefaultSession(): String = "session_${System.currentTimeMillis()}"
        
        fun createChatRequest(
            message: String,
            systemPrompt: String = "You are a helpful assistant",
            temperature: Double = 0.7,
            reasoningEffort: String = "medium",
            enableBrowsing: Boolean = true
        ): GradioRequest {
            return GradioRequest(
                fn_index = 0,
                data = listOf(message, systemPrompt, temperature, reasoningEffort, enableBrowsing),
                session_hash = generateDefaultSession()
            )
        }
    }
}

data class GradioResponse(
    val data: List<String>? = null,
    val error: String? = null,
    val success: Boolean? = null,
    val duration: Double? = null
) {
    fun getResponseText(): String {
        return when {
            error != null -> "Error: $error"
            data?.isNotEmpty() == true -> data.first()
            else -> "No response received from AI"
        }
    }
}
```


### app/src/main/java/com/gptoss/chat/api/ApiClient.kt

```kotlin
package com.gptoss.chat.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private const val BASE_URL = "https://amd-gpt-oss-120b-chatbot.hf.space/"
        private const val PREDICT_ENDPOINT = "api/predict"
        private const val TAG = "ApiClient"
    }

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d(TAG, message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val gradioApiService = retrofit.create(GradioApiService::class.java)

    suspend fun sendMessage(
        message: String,
        systemPrompt: String = "You are a helpful assistant",
        temperature: Double = 0.7,
        reasoningEffort: String = "medium",
        enableBrowsing: Boolean = true
    ): String = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Sending message: $message")

            val request = GradioRequest.createChatRequest(
                message = message,
                systemPrompt = systemPrompt,
                temperature = temperature,
                reasoningEffort = reasoningEffort,
                enableBrowsing = enableBrowsing
            )

            val response = gradioApiService.sendMessage(PREDICT_ENDPOINT, request)

            if (response.isSuccessful) {
                val gradioResponse = response.body()
                val responseText = gradioResponse?.getResponseText() ?: "Empty response"
                Log.d(TAG, "Received response: $responseText")
                return@withContext responseText
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "API Error: ${response.code()} - $errorBody")
                throw ApiException("API request failed with code: ${response.code()}", response.code())
            }

        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Network error", e)
            throw NetworkException("Network error: ${e.message}", e)
        }
    }

    suspend fun sendMessageWithFallback(message: String): String {
        return try {
            sendMessage(message)
        } catch (e: Exception) {
            Log.w(TAG, "Primary API call failed, trying fallback", e)
            sendMessageDirectHttp(message)
        }
    }

    private suspend fun sendMessageDirectHttp(message: String): String = withContext(Dispatchers.IO) {
        try {
            val requestData = mapOf(
                "fn_index" to 0,
                "data" to listOf(
                    message,
                    "You are a helpful assistant",
                    0.7,
                    "medium",
                    true
                ),
                "session_hash" to GradioRequest.generateDefaultSession()
            )

            val jsonRequest = gson.toJson(requestData)
            val requestBody = jsonRequest.toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url("${BASE_URL}${PREDICT_ENDPOINT}")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "GPT-OSS-Chat/1.0")
                .build()

            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    val gradioResponse = gson.fromJson(responseBody, GradioResponse::class.java)
                    return@withContext gradioResponse.getResponseText()
                }
            }

            throw ApiException("Direct HTTP request failed: ${response.code}", response.code)

        } catch (e: Exception) {
            throw NetworkException("Fallback network error: ${e.message}", e)
        }
    }
}

class ApiException(message: String, val code: Int) : Exception(message)
class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)
```


### app/src/main/java/com/gptoss/chat/api/ApiManager.kt

```kotlin
package com.gptoss.chat.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiManager(private val context: Context) {
    private val apiClient = ApiClient()

    suspend fun sendChatMessage(message: String): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (!isNetworkAvailable()) {
                Result.failure(NetworkException("No internet connection"))
            } else {
                val response = apiClient.sendMessageWithFallback(message)
                Result.success(response)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
               networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    companion object {
        @Volatile
        private var INSTANCE: ApiManager? = null

        fun getInstance(context: Context): ApiManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
```


***

## 🚀 7. GitHub Actions Workflow

### .github/workflows/build-and-release.yml

```yaml
name: Build and Release Android APK

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

env:
  JAVA_VERSION: '17'
  GRADLE_VERSION: '8.9'

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - name: Checkout Repository
      uses: actions/checkout@v4
      with:
        fetch-depth: 0

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: ${{ env.JAVA_VERSION }}
        distribution: 'temurin'
        cache: gradle

    - name: Cache Gradle Dependencies
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: gradle-${{ runner.os }}-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          gradle-${{ runner.os }}-

    - name: Setup Gradle
      uses: gradle/gradle-build-action@v3

    - name: Make gradlew executable
      run: chmod +x ./gradlew || echo "gradlew not found, will generate"

    - name: Generate Gradle Wrapper if missing
      run: |
        if [ ! -f "./gradlew" ]; then
          echo "Gradle wrapper not found, generating..."
          gradle wrapper --gradle-version=${{ env.GRADLE_VERSION }}
          chmod +x ./gradlew
          echo "Gradle wrapper generated successfully"
        else
          echo "Gradle wrapper found, continuing..."
        fi

    - name: Verify Gradle Installation
      run: ./gradlew --version

    - name: Decode Android Keystore
      if: github.ref == 'refs/heads/main'
      env:
        ENCODED_KEYSTORE: ${{ secrets.ANDROID_KEYSTORE }}
      run: |
        if [ -z "$ENCODED_KEYSTORE" ]; then
          echo "Warning: ANDROID_KEYSTORE secret not found"
          echo "Building unsigned APK..."
        else
          echo $ENCODED_KEYSTORE | base64 -d > keystore.jks
          echo "Keystore decoded successfully"
        fi

    - name: Build Debug APK
      run: ./gradlew assembleDebug --stacktrace --no-daemon

    - name: Build Release APK (Signed)
      if: github.ref == 'refs/heads/main' && github.event_name == 'push'
      env:
        KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
        KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
        KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
      run: |
        if [ -f "keystore.jks" ]; then
          echo "Building signed release APK..."
          ./gradlew assembleRelease --stacktrace --no-daemon
        else
          echo "No keystore found, building unsigned release APK..."
          ./gradlew assembleRelease --stacktrace --no-daemon
        fi

    - name: Upload Debug APK as Artifact
      uses: actions/upload-artifact@v4
      with:
        name: gpt-oss-debug-apk
        path: app/build/outputs/apk/debug/app-debug.apk
        retention-days: 30

    - name: Upload Release APK as Artifact
      uses: actions/upload-artifact@v4
      with:
        name: gpt-oss-release-apk
        path: app/build/outputs/apk/release/app-release.apk
        retention-days: 90

    - name: Get Version Info
      id: version
      run: |
        echo "date=$(date +'%Y%m%d_%H%M%S')" >> $GITHUB_OUTPUT
        echo "version=v1.0.${GITHUB_RUN_NUMBER}" >> $GITHUB_OUTPUT
        echo "commit_short=$(git rev-parse --short HEAD)" >> $GITHUB_OUTPUT

    - name: Create Release
      if: github.ref == 'refs/heads/main' && github.event_name == 'push'
      id: create_release
      uses: softprops/action-gh-release@v1
      with:
        tag_name: ${{ steps.version.outputs.version }}
        name: GPT-OSS Chat ${{ steps.version.outputs.version }}
        body: |
          ## 🚀 GPT-OSS Chat Release ${{ steps.version.outputs.version }}
          
          **Build Date:** ${{ steps.version.outputs.date }}
          **Commit:** ${{ steps.version.outputs.commit_short }}
          
          ### 📱 Installation Instructions:
          1. Download the APK file below
          2. Enable "Install from Unknown Sources" in your Android settings
          3. Install the APK file
          4. Launch GPT-OSS Chat and start chatting!
          
          ### 🔧 Technical Details:
          - **API Integration:** Hugging Face amd/gpt-oss-120b-chatbot
          - **Build System:** Gradle ${{ env.GRADLE_VERSION }}
          - **Target SDK:** 34 (Android 14)
          - **Minimum SDK:** 24 (Android 7.0)
          
          ### 📝 Changes:
          ${{ github.event.head_commit.message }}
          
          ---
          Built automatically with GitHub Actions ⚡
        draft: false
        prerelease: false
        files: app/build/outputs/apk/release/app-release.apk
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

    - name: Upload APK to Release
      if: github.ref == 'refs/heads/main' && github.event_name == 'push'
      uses: softprops/action-gh-release@v1
      with:
        tag_name: ${{ steps.version.outputs.version }}
        files: |
          app/build/outputs/apk/release/app-release.apk
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

  notify:
    needs: build
    runs-on: ubuntu-latest
    if: always()
    steps:
    - name: Notify Build Result
      run: |
        if [ "${{ needs.build.result }}" == "success" ]; then
          echo "✅ Build completed successfully!"
        else
          echo "❌ Build failed!"
        fi
```


***

## 🔧 8. ملفات إضافية

### gradle/wrapper/gradle-wrapper.properties

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.9-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```


### app/proguard-rules.pro

```pro
# Add project specific ProGuard rules here.
-keep class com.gptoss.chat.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.google.gson.** { *; }

-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

-keepattributes Signature
-keepattributes *Annotation*

-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
```


### README.md

```markdown
# GPT-OSS Chat

An Android chat application that provides a front-end interface for the amd/gpt-oss-120b-chatbot model on Hugging Face.

## Features

- Simple and intuitive chat interface
- Real-time messaging with AI
- Message history with timestamps
- Material Design UI
- Automated builds via GitHub Actions

## Setup Instructions

### 1. Repository Secrets

Create the following secrets in your GitHub repository:

- `ANDROID_KEYSTORE`: Base64 encoded keystore file
- `KEYSTORE_PASSWORD`: Password for the keystore
- `KEY_ALIAS`: Alias name for the signing key
- `KEY_PASSWORD`: Password for the signing key

### 2. Generate Signing Keystore

```

keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias gptoss-key

```

Then encode to base64:
```

base64 -i keystore.jks | pbcopy  \# macOS
base64 -i keystore.jks | xclip -selection clipboard  \# Linux

```

### 3. Build Process

The GitHub Actions workflow will automatically:
1. Build the APK on every push to main
2. Sign the release APK using your keystore
3. Upload APK as GitHub Artifacts
4. Create a new release with the APK attached

## Technical Stack

- **Language**: Kotlin
- **UI**: Android Views with ViewBinding
- **Architecture**: MVVM pattern
- **Networking**: Retrofit + OkHttp
- **API**: Hugging Face Gradio Client
- **Build System**: Gradle (Kotlin DSL)
- **CI/CD**: GitHub Actions

## Installation

1. Download the APK from the latest release
2. Enable "Install from Unknown Sources" in Android settings
3. Install the APK file
4. Grant necessary permissions when prompted

## Usage

1. Open the app
2. Type your message in the input field
3. Tap "Send" to chat with the AI
4. View conversation history in the scrollable list

## License

This project is open source and available under the MIT License.
```


***

## 🎯 خطوات التشغيل النهائية

1. **إنشاء مستودع GitHub جديد** باسم "GPT-OSS"
2. **نسخ جميع الملفات أعلاه** بالهيكل المحدد
3. **إنشاء Android Keystore** وإضافة GitHub Secrets
4. **Push إلى main branch** لبدء البناء التلقائي
5. **تحميل APK** من Releases أو Artifacts

**🎉 تهانينا! مشروع GPT-OSS Chat جاهز للاستخدام والنشر!**
<span style="display:none">[^1]</span>

<div style="text-align: center">⁂</div>

[^1]: GEMINI.md


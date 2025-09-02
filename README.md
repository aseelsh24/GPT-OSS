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

base64 -i keystore.jks | pbcopy  # macOS
base64 -i keystore.jks | xclip -selection clipboard  # Linux

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

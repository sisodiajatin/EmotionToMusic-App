# Uphoria: Emotion Based Music Recommendation App 🎵😊

An intelligent Android application that analyzes facial expressions to detect user emotions and curates personalized music recommendations based on the detected emotional state. Built with Kotlin and modern Android development practices.

![Uphoria Demo](https://via.placeholder.com/800x400/6C5CE7/ffffff?text=Uphoria+-+Emotion+Based+Music+App)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)



## 🚀 Features

### Core Functionality
- **Real-time Emotion Detection**: Advanced facial expression recognition using DeepFace
- **Intelligent Music Recommendations**: Curated songs based on detected emotions via Last.fm and Spotify APIs
- **Native Android Experience**: Built with Kotlin and modern Android architecture components
- **Privacy-focused Design**: Secure image processing with minimal data retention
- **Seamless Integration**: Direct Spotify playback integration

### Emotion Recognition
- **Supported Emotions**: Happy 😊, Sad 😢, Angry 😠, Surprised 😮, Fearful 😨, Disgusted 🤢, Neutral 😐
- **Advanced ML Processing**: DeepFace library for accurate emotion classification
- **Confidence Scoring**: Real-time accuracy indicators for predictions
- **Facial Action Units**: FACS-based expression analysis

### Music Features
- **Smart Genre Mapping**: Emotion-to-genre algorithm with weighted scoring
- **Multi-API Integration**: Last.fm for recommendations + Spotify for metadata
- **High-Quality Artwork**: Album covers and artist information
- **Direct Playback**: One-tap Spotify integration
- **Feedback System**: Like/dislike mechanism for improved recommendations

## 🛠️ Technology Stack

### Android Frontend
- **Kotlin** - Primary development language
- **CameraX** - Modern camera API for consistent capture
- **Retrofit + OkHttp** - Type-safe HTTP client with coroutines
- **Material Design Components** - Modern UI framework
- **Glide** - Efficient image loading and caching
- **Kotlin Coroutines** - Asynchronous programming

### Backend Services
- **Python Flask** - RESTful API server
- **DeepFace** - Emotion detection and facial analysis
- **Ngrok** - Development tunneling for mobile connectivity

### External APIs
- **Last.fm API** - Music track recommendations and metadata
- **Spotify Web API** - Enhanced metadata and playback URLs
- **OAuth2** - Secure authentication flow

## 📦 Installation

### Prerequisites
- **Android Studio** (Arctic Fox or newer)
- **Android SDK** (API 21+)
- **Python 3.8+** (for backend server)
- **Spotify Developer Account**
- **Last.fm API Account**

### Android App Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/sisodiajatin/Uphoria-EmotionBasedMusicApp.git
   cd Uphoria-EmotionBasedMusicApp
   ```

2. **Open in Android Studio**
   ```bash
   # Open the project in Android Studio
   # Sync Gradle files and resolve dependencies
   ```

3. **Configure dependencies**
   ```kotlin
   // app/build.gradle dependencies are pre-configured
   implementation 'androidx.camera:camera-camera2:1.3.0'
   implementation 'com.squareup.retrofit2:retrofit:2.9.0'
   implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4'
   ```

4. **Update API endpoints**
   ```kotlin
   // Update BASE_URL in EmotionApiService
   companion object {
       const val BASE_URL = "https://your-ngrok-url.ngrok.io/"
   }
   ```

### Backend Server Setup

1. **Install Python dependencies**
   ```bash
   cd backend
   pip install flask deepface requests python-dotenv
   ```

2. **Set up environment variables**
   ```bash
   # Create .env file
   SPOTIFY_CLIENT_ID=your_spotify_client_id
   SPOTIFY_CLIENT_SECRET=your_spotify_client_secret
   LASTFM_API_KEY=your_lastfm_api_key
   ```

3. **Start the Flask server**
   ```bash
   python app.py
   ```

4. **Set up Ngrok tunneling**
   ```bash
   # Install ngrok and create public tunnel
   ngrok http 5000
   # Copy the HTTPS URL to your Android app
   ```

### API Keys Configuration

#### Spotify API Setup
1. Go to [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Create a new app and get Client ID & Secret
3. Add to backend `.env` file

#### Last.fm API Setup
1. Register at [Last.fm API](https://www.last.fm/api/account/create)
2. Create application and get API key
3. Add to backend `.env` file

## 🎯 Usage

### Getting Started

1. **Launch the App**
   - Grant camera permissions when prompted
   - Log in using provided credentials (stored in CSV)

2. **Capture Your Emotion**
   - Position your face in the camera viewfinder
   - Tap the capture button (FloatingActionButton)
   - Wait for emotion processing

3. **Receive Music Recommendation**
   - View your detected emotion with confidence score
   - See recommended song with artist and album art
   - Tap "Open in Spotify" for direct playback

4. **Provide Feedback**
   - Use like/dislike buttons to improve future recommendations
   - Navigate back to capture another emotion

### Advanced Features

#### Authentication System
```kotlin
// CSV-based credential verification
// Credentials stored in app/src/main/assets/credentials.csv
username,password
testuser,password123
demo,demo123
```

#### Camera Integration
```kotlin
// CameraX implementation with lifecycle awareness
private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        bindPreview(cameraProvider)
    }, ContextCompat.getMainExecutor(this))
}
```

## 📊 API Documentation

### Emotion Detection Endpoint

#### POST `/detect_emotion`
Upload image for emotion analysis

**Request:**
```http
POST /detect_emotion
Content-Type: multipart/form-data

file: [image_file.jpg]
```

**Response:**
```json
{
  "dominant_emotion": "happy",
  "recommended_song": {
    "name": "Happy Song Title",
    "artist": "Artist Name",
    "spotify_url": "https://open.spotify.com/track/...",
    "image_url": "https://album-art-url.jpg"
  },
  "error": null
}
```

### Music Recommendation Algorithm

#### Emotion-to-Genre Mapping
```python
EMOTION_GENRE_MAP = {
    'happy': ['pop', 'dance', 'electronic', 'indie'],
    'sad': ['blues', 'acoustic', 'ambient', 'folk'],
    'angry': ['rock', 'metal', 'punk', 'hardcore'],
    'surprised': ['electronic', 'pop', 'experimental'],
    'fear': ['ambient', 'dark', 'atmospheric'],
    'disgust': ['alternative', 'grunge', 'industrial'],
    'neutral': ['chill', 'lounge', 'instrumental']
}
```

#### Scoring Algorithm
```python
def calculate_genre_scores(emotion_scores):
    # Normalize emotion scores to sum to 1
    normalized_scores = normalize_emotions(emotion_scores)
    
    # Apply 1.5x weight to dominant emotion
    dominant_emotion = max(emotion_scores, key=emotion_scores.get)
    normalized_scores[dominant_emotion] *= 1.5
    
    # Calculate genre probabilities
    genre_scores = {}
    for emotion, score in normalized_scores.items():
        for genre in EMOTION_GENRE_MAP[emotion]:
            genre_scores[genre] = genre_scores.get(genre, 0) + score
    
    return max(genre_scores, key=genre_scores.get)
```

## 🧪 Testing

### Running Tests

```bash
# Android Unit Tests
./gradlew test

# Android Instrumentation Tests
./gradlew connectedAndroidTest

# Backend Tests
cd backend
python -m pytest tests/
```

### Test Coverage

#### Android Tests
- **Unit Tests**: Data model validation, API service testing
- **Integration Tests**: Camera functionality, network operations
- **UI Tests**: Activity navigation, user interaction flows

#### Backend Tests
- **Emotion Detection**: Algorithm accuracy validation
- **API Integration**: Last.fm and Spotify connectivity
- **Recommendation Logic**: Genre mapping algorithm verification

## 📱 Project Structure

```
Uphoria-EmotionBasedMusicApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/uphoria/
│   │   │   ├── LoginActivity.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── ResultActivity.kt
│   │   │   ├── EmotionApiService.kt
│   │   │   └── data/
│   │   │       ├── DetectResponse.kt
│   │   │       └── RecommendedSong.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   └── drawable/
│   │   └── assets/
│   │       └── credentials.csv
├── backend/
│   ├── app.py
│   ├── emotion_service.py
│   ├── music_service.py
│   └── requirements.txt
└── docs/
    ├── architecture.md
    └── api_documentation.md
```

## 🔒 Privacy & Security

### Data Protection
- **Image Processing**: Images sent to server for analysis, not permanently stored
- **Local Authentication**: CSV-based credentials (development only)
- **Secure Communication**: HTTPS-only API communication
- **Minimal Data Collection**: Only necessary emotion data collected

### Security Measures
- **Runtime Permissions**: Proper Android permission handling
- **API Key Security**: Environment variable management
- **Input Validation**: Server-side image validation
- **Error Handling**: Secure error messages without sensitive data exposure

## 🚀 Deployment

### Development Deployment

#### Backend Server
```bash
# Start development server
cd backend
export FLASK_ENV=development
python app.py

# Start ngrok tunnel
ngrok http 5000
```

#### Android App
```bash
# Debug build
./gradlew assembleDebug

# Install on connected device
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Production Considerations

#### Server Deployment
- **Cloud Hosting**: AWS EC2, Google Cloud Platform, or Heroku
- **Load Balancing**: Nginx for multiple server instances
- **Database**: PostgreSQL for user data and analytics
- **Monitoring**: Application performance monitoring

#### Mobile Deployment
- **Google Play Store**: Production release build
- **Code Signing**: Android app signing for distribution
- **ProGuard**: Code obfuscation for release builds

## 🤝 Contributing

### Development Workflow

1. **Fork the repository**
2. **Create feature branch**
   ```bash
   git checkout -b feature/emotion-detection-improvement
   ```
3. **Follow coding standards**
   - Kotlin style guide
   - Material Design principles
   - Clean architecture patterns
4. **Test thoroughly**
5. **Submit pull request**

### Code Style Guidelines
- **Kotlin**: Follow official Kotlin coding conventions
- **Android**: Material Design and Architecture Components
- **Comments**: KDoc for public APIs
- **Naming**: Descriptive variable and function names

## 📄 Research Background

This project demonstrates the application of emotion recognition and music recommendation systems:

### Key Technical Areas
- **Facial Expression Recognition**: FACS-based emotion classification
- **Machine Learning**: Deep learning models for emotion detection
- **Music Information Retrieval**: Content-based recommendation algorithms
- **Mobile Development**: Modern Android architecture and design patterns

### Academic Foundation
- Face Detection and Facial Expression Recognition systems
- Emotional Recognition using computer vision techniques
- Music Recommendation Systems with contextual information
- Human-Computer Interaction for emotion-aware interfaces


## 🗺️ Future Enhancements

### Version 2.0 Roadmap
- [ ] **Real-time Processing**: Live emotion detection during music playback
- [ ] **User Profiles**: Personalized recommendation learning
- [ ] **Social Features**: Playlist sharing and collaborative filtering
- [ ] **Voice Integration**: Voice command support
- [ ] **Offline Mode**: Local emotion detection and cached recommendations

### Technical Improvements
- [ ] **ML Optimization**: Custom emotion detection models
- [ ] **Performance**: Reduced latency and battery optimization
- [ ] **Security**: Enhanced authentication and data encryption
- [ ] **Analytics**: Detailed usage analytics and A/B testing
- [ ] **Cross-platform**: Flutter or React Native implementation

## 🏆 Key Features Highlights

### Innovation Points
- **Seamless UX**: One-tap emotion detection to music playback
- **Intelligent Mapping**: Advanced emotion-to-genre algorithm
- **Modern Architecture**: Clean architecture with MVVM pattern
- **Performance Optimized**: Efficient camera and network operations
- **Privacy First**: Local processing with minimal data collection

### Technical Excellence
- **Type Safety**: Kotlin's null safety and strong typing
- **Asynchronous**: Coroutines for smooth user experience
- **Material Design**: Modern UI/UX following Google guidelines
- **Modular Code**: Clean separation of concerns
- **Comprehensive Testing**: Unit, integration, and UI tests

---

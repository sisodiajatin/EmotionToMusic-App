┌─────────────────────────────────────────────────────────────────┐
│                    ANDROID CLIENT LAYER                        │
├─────────────────────────────────────────────────────────────────┤
│  Android Application (Kotlin)                                  │
│  ├── Login Activity (CSV-based Authentication)                 │
│  ├── Main Activity (Camera Integration + CameraX)              │
│  ├── Result Activity (Music Recommendation Display)            │
│  └── UI Components (Material Design)                           │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                     NETWORK LAYER                              │
├─────────────────────────────────────────────────────────────────┤
│  Retrofit + OkHttp + Kotlin Coroutines                         │
│  ├── EmotionApiService Interface                               │
│  ├── Multipart Image Upload                                    │
│  ├── JSON Serialization (Gson)                                 │
│  └── Asynchronous Network Operations                           │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    BACKEND SERVER                              │
├─────────────────────────────────────────────────────────────────┤
│  Python Flask Server                                           │
│  ├── Emotion Detection Service (DeepFace Library)              │
│  ├── Genre Mapping Engine                                      │
│  ├── Music Recommendation Service                              │
│  └── RESTful API Endpoints                                     │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    EXTERNAL APIS                               │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │   Last.fm API   │  │  Spotify API    │  │   Ngrok Tunnel  │ │
│  │                 │  │                 │  │                 │ │
│  │ • Genre tracks  │  │ • OAuth2 flow   │  │ • Public URL    │ │
│  │ • Metadata      │  │ • Search API    │  │ • Development   │ │
│  │ • Caching       │  │ • Cover art     │  │   connectivity  │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
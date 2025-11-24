# CourseProject-SecureNotewithOAuth2
SecureNoteAndroidAppwithOauth2
SecureNote Android App
SecureNote is a modular Android application that simulates a secure note-taking experience using OAuth-style token authentication and Postman mock APIs. It demonstrates real-world architecture for login, token storage, authenticated API calls, and secure note management — all without a real backend.

Features
- Secure Login using client credentials and token-based authentication
- View Notes fetched from a mock API with bearer token
- Create Notes via a secure POST request
- Logout with token clearing and session reset
- Fragment-based Navigation with ViewBinding
- Postman Mock Server Integration for simulating backend behavior

Tech Stack
Layer                        Technology
Language                     Java
UI                           Android Fragments, ViewBinding
Networking                   OkHttp
JSON Parsing                 Gson
API Simulation               Postman Mock Server
Token Storage                SharedPreferences via TokenManager



App Flow
LoginFragment
   ? (POST /oauth/token)
TokenManager saves access_token
   ?
NotesFragment
   ? (GET /notes with Authorization: Bearer <token>)
ListView displays notes
   ?
[+ Add Note] ? AddNoteFragment
   ? (POST /notes with JSON body)
? Navigate back to NotesFragment
   ? (onResume ? GET /notes again)
Updated list shown
   ?
[Logout] ? Clears token ? LoginFragment



Postman Mock Server Setup
Endpoints

Method    Path            Description
POST      /oauth/token    Returns a static access token
GET       /notes          Returns a list of notes
POST     /notes           Simulates note creation 


Matching Rules
- Authorization = Bearer abc123 for all authenticated requests
- POST /notes expects a JSON body:
{
  "title": "Note title",
  "content": "Note content"
}


Postman mock servers are stateless — new notes won’t persist unless manually added to the GET /notes example.


Project Structure

com.example.oauthsecurenoteapp
├── LoginFragment.java        # Handles login flow (POST /oauth/token)
├── NotesFragment.java        # Displays notes list (GET /notes)
├── AddNoteFragment.java      # Adds new notes (POST /notes)
├── ApiService.java           # API client (OkHttp requests to mock server)
├── TokenManager.java         # Manages token storage in SharedPreferences
├── Note.java                 # Model class for notes
└── res/
    └── layout/
        ├── fragment_login.xml
        ├── fragment_notes.xml
        ├── fragment_add_note.xml
        └── item_note.xml




How to Run
- Clone the repo and open in Android Studio
- Replace BASE_URL in ApiService.java with your Postman mock server URL
- Run the app on an emulator or device
- Use any credentials to log in (Postman mock accepts all)
- View, add, and simulate secure notes

What You’ll Learn
- Token-based authentication flow
- OkHttp request building and error handling
- Fragment lifecycle and safe UI updates
- Postman mock server design and matching
- Stateless API simulation for rapid prototyping

Future Enhancements (Optional)
- Room database for offline note storage
- RecyclerView with swipe-to-delete
- Note editing and search



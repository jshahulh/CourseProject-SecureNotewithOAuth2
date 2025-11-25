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

OAuth 2.0 Flow Explained
1. End User / Resource Owner
•  This is the person using the app (e.g., SecureNote user).
•	They initiate the login or authorization process.
2. Authorization Request
•	The app sends a request to the Authorization Server asking for permission to access the user’s data.
•	This usually opens a login screen or consent form.
3. User Authorization
•	The user approves the request (e.g., logs in or grants access).
•	The Authorization Server confirms the user’s identity and permission.
4. Access Token Request
•	The app now asks the Authorization Server for an access token.
•	This token is a credential that proves the app has permission to access resources.
5. Access Token
•	The Authorization Server returns the token to the app.
•	This token is used in future API calls to authenticate the app.
6. Resource Request
•	The app sends a request to the Resource Server (e.g., to fetch notes, user data).
•	It includes the access token in the request header (usually as Authorization: Bearer).
7. Resource Access
•	The Resource Server validates the token.
•	If valid, it returns the requested data to the app.

OAuth 2.0 Step              	   SecureNote Component                                	Description                                                                 
Authorization Request  	         LoginFragment → POST /oauth/token	                  User enters credentials; app requests token from Postman mock server
User Authorization	            Mock server returns static token	                     No real user consent — mock server simulates approval
Access Token Request	            ApiService.java- handles token request	               OkHttp sends request; TokenManager stores token
Access Token	                  TokenManager saves token in SharedPreference	         Token (`abc123`) used for future requests
Resource Request	               NotesFragment → GET /notes	                           App fetches notes using `Authorization: Bearer abc123`
Resource Access	               Mock server returns note list	                        Notes displayed in UI
Create Resource	               AddNoteFragment → `POST /notes`             	         App sends new note JSON to mock server                                    
Logout	                       `TokenManager.clearToken()` → `LoginFragment`	         Token cleared; user returned to login screen  



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
├── MainActivity.java         # Hosts fragments, entry point of app
├── LoginFragment.java        # Handles login flow (POST /oauth/token)
├── NotesFragment.java        # Displays notes list (GET /notes)
├── AddNoteFragment.java      # Adds new notes (POST /notes)
├── ApiService.java           # API client (OkHttp requests to mock server)
├── TokenManager.java         # Manages token storage in SharedPreferences
├── Note.java                 # Model class for notes
└── res/
    └── layout/
        ├── activity_main.xml  -  # // Main layout with FrameLayout
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



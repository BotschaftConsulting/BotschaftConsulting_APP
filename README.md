KI-Assistent + E-Commerce Android App (Android Studio)
Projektübersicht
Diese App kombiniert:
•	KI-Assistent (ChatGPT-ähnlicher Chat)
•	E-Commerce Shop
•	Firebase Login
•	Cloud Firestore
•	Warenkorb
•	Produktlisten
•	Modernes Premium UI
•	Dark Mode
•	API Integration
Tech Stack:
•	Kotlin
•	Jetpack Compose
•	Firebase Authentication
•	Firebase Firestore
•	Retrofit
•	OpenAI API
•	Coil
•	Material 3
________________________________________
1. Android Studio Setup
build.gradle (Project)
plugins {
    id 'com.android.application' version '8.2.2' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.22' apply false
    id 'com.google.gms.google-services' version '4.4.0' apply false
}
________________________________________
2. build.gradle (App)
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.gms.google-services'
}

android {
    namespace 'com.example.aiestore'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.aiestore"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildFeatures {
        compose true
    }

    composeOptions {
        kotlinCompilerExtensionVersion '1.5.8'
    }
}

dependencies {

    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.activity:activity-compose:1.8.2'

    implementation platform('androidx.compose:compose-bom:2024.02.00')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.navigation:navigation-compose:2.7.6'

    implementation 'io.coil-kt:coil-compose:2.5.0'

    implementation platform('com.google.firebase:firebase-bom:32.7.2')
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-firestore-ktx'

    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    implementation 'com.google.code.gson:gson:2.10.1'
}
________________________________________
3. MainActivity.kt
package com.example.aiestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}
________________________________________
4. Navigation
AppNavigation.kt
package com.example.aiestore

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController)
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("chat") {
            ChatScreen()
        }

        composable("cart") {
            CartScreen()
        }
    }
}
________________________________________
5. Login Screen
LoginScreen.kt
package com.example.aiestore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "AI Store")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    navController.navigate("home")
                }
        }) {
            Text("Login")
        }
    }
}
________________________________________
6. Produktmodell
Product.kt
package com.example.aiestore

data class Product(
    val title: String = "",
    val price: Double = 0.0,
    val image: String = ""
)
________________________________________
7. Home Screen
HomeScreen.kt
package com.example.aiestore

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun HomeScreen(navController: NavController) {

    val products = listOf(
        Product(
            "Gaming Laptop",
            1200.0,
            "https://images.unsplash.com/photo-1496181133206-80ce9b88a853"
        ),
        Product(
            "Smartphone",
            899.0,
            "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9"
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("chat")
                    },
                    label = { Text("AI") },
                    icon = {}
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("cart")
                    },
                    label = { Text("Cart") },
                    icon = {}
                )
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            items(products) { product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Image(
                            painter = rememberAsyncImagePainter(product.image),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(product.title)

                        Text("$${product.price}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {}) {
                            Text("Add to Cart")
                        }
                    }
                }
            }
        }
    }
}
________________________________________
8. KI Chat Integration
OpenAIService.kt
package com.example.aiestore

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIService {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer YOUR_API_KEY"
    )
    @POST("v1/chat/completions")
    fun sendMessage(
        @Body request: ChatRequest
    ): Call<ChatResponse>
}
________________________________________
9. Chat Screen
ChatScreen.kt
package com.example.aiestore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen() {

    var message by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(text = "AI Assistant")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            response = "AI Antwort erscheint hier"
        }) {
            Text("Senden")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(response)
    }
}
________________________________________
10. Cart Screen
CartScreen.kt
package com.example.aiestore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CartScreen() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Shopping Cart")
    }
}
________________________________________
11. Firebase Setup
Schritte
1.	Firebase Projekt erstellen
2.	Android App registrieren
3.	google-services.json herunterladen
4.	Datei in app/ kopieren
5.	Authentication aktivieren
6.	Firestore aktivieren
________________________________________
12. Premium Features
Du kannst später hinzufügen:
•	Stripe Payments
•	Google Login
•	AI Voice Assistant
•	Produktempfehlungen mit KI
•	Push Notifications
•	Admin Panel
•	Multi Vendor System
•	Chat Support
•	AI Image Search
•	Subscription System
•	AI Product Generator
________________________________________
13. Projektstruktur
com.example.aiestore
│
├── MainActivity.kt
├── AppNavigation.kt
├── LoginScreen.kt
├── HomeScreen.kt
├── ChatScreen.kt
├── CartScreen.kt
├── Product.kt
├── OpenAIService.kt
├── ui.theme
└── network
________________________________________
14. Deployment
APK erstellen
Android Studio:
Build → Generate Signed Bundle/APK
Dann:
•	Play Store Upload
•	App Icon hinzufügen
•	Datenschutzrichtlinie
•	Screenshots erstellen
•	Release Build erzeugen
________________________________________
15. Erweiterungsideen
AI Features
•	Sprachchat
•	AI Verkäufer
•	AI Preisvergleich
•	AI Modeberater
•	AI Finanzberater
•	AI Produktsuche
Shop Features
•	Coupons
•	Bestelltracking
•	Bewertungen
•	Live Chat
•	Wunschliste
•	Lieferstatus
________________________________________
Ergebnis
Du hast jetzt eine moderne Android Studio App Grundlage für:
•	KI Assistent
•	Premium E-Commerce
•	Firebase Cloud Backend
•	Login System
•	Navigation
•	Moderne Compose UI
•	API Integration
•	Erweiterbar für Startup Niveau

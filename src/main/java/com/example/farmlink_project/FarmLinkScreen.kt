package com.example.farmlink_project

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.foundation.border
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FarmLinkScreen(navController: NavHostController) {
    var navigateToSignIn by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    Log.d("AddToCart", "User ID: $userId")


    // Delay for 3 seconds before navigating
    LaunchedEffect(Unit) {
        delay(3000)
        navigateToSignIn = true
    }

    if (navigateToSignIn) {
        SignInLayout(navController = navController)
    } else {
        GreetingWithImage(
            message = "Farm\nLink",
            from = "From the Soil, to your Soul",
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun GreetingWithImage(message: String, from: String, modifier: Modifier = Modifier) {
    val customFontFamily = FontFamily(Font(R.font.font1))

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.plant_logo),
                contentDescription = "Plant Logo",
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 10.dp)
            )

            Text(
                text = "Farm",
                fontSize = 32.sp,
                color = Color(0xFF006400),
                fontFamily = customFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Text(
                text = "Link",
                fontSize = 32.sp,
                color = Color(0xFF006400),
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        val text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black)) {
                append("From the Soil, to your ")
            }
            withStyle(style = SpanStyle(color = Color(0xFFB8860B))) {
                append("Soul")
            }
        }

        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = customFontFamily,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun SignInLayout(navController: NavHostController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.plant_logo),
            contentDescription = "Plant Logo",
            modifier = Modifier.size(250.dp).padding(bottom = 20.dp)
        )

        BasicTextField(
            value = email.value,
            onValueChange = { email.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.Gray),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (email.value.isEmpty()) Text(text = "Email", color = Color.Gray)
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = password.value,
            onValueChange = { password.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.Gray),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (password.value.isEmpty()) Text(text = "Password", color = Color.Gray)
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show error message if login fails
        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        val auth = FirebaseAuth.getInstance()

        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("home_screen")  // Navigate to home if login is successful
                        } else {
                            errorMessage.value = "Invalid email or password"
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign In")
        }


        Spacer(modifier = Modifier.height(8.dp))

        // Clickable text to navigate to the Register screen
        TextButton(onClick = { navController.navigate("register_screen") }) {
            Text("Don't have an account? Register here", color = Color.Blue)
        }
    }
}


package com.example.farmlink_project

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterLayout(navController: NavHostController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val selectedRole = remember { mutableStateOf<String?>(null) }
    val errorMessage = remember { mutableStateOf("") }
    val successMessage = remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.plant_logo),
            contentDescription = "Plant Logo",
            modifier = Modifier.size(200.dp).padding(bottom = 20.dp)
        )

        Text(
            text = "Register",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
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

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(1.dp, Color.Gray),
            decorationBox = { innerTextField ->
                Box(Modifier.padding(16.dp)) {
                    if (confirmPassword.value.isEmpty()) Text(text = "Confirm Password", color = Color.Gray)
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Select Role", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { selectedRole.value = "Buyer" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedRole.value == "Buyer") Color.Green else Color.Gray
                )
            ) { Text("Buyer") }

            Button(
                onClick = { selectedRole.value = "Seller" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedRole.value == "Seller") Color.Green else Color.Gray
                )
            ) { Text("Seller") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.value.isNotEmpty()) {
            Text(
                text = errorMessage.value,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (successMessage.value.isNotEmpty()) {
            Text(
                text = successMessage.value,
                color = Color.Green,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(onClick = {
            when {
                email.value.isEmpty() || password.value.isEmpty() || confirmPassword.value.isEmpty() || selectedRole.value == null -> {
                    errorMessage.value = "All fields are required"
                }
                password.value != confirmPassword.value -> {
                    errorMessage.value = "Passwords do not match"
                }
                else -> {
                    auth.createUserWithEmailAndPassword(email.value, password.value)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                successMessage.value = "Registration successful!"
                                errorMessage.value = ""
                                navController.navigate("farmlink_screen")
                            } else {
                                errorMessage.value = "Registration failed: ${task.exception?.message}"
                            }
                        }
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Register")
        }

    }
}

package com.example.farmlink_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
//import androidx.compose.runtime.snapshots.SnapshotStateList
//import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

class ChatScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatScreen(navController = null)
        }
    }
}

@Composable
fun ChatScreen(navController: NavHostController?) {
    val messages = remember { mutableStateOf(listOf<Pair<String, Boolean>>()) } // List of messages, Boolean indicates if it's user or system

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F5EE))
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController?.popBackStack() },
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Chats",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Farmer Profile
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = "Farmer Profile",
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFCCE5FF), shape = RoundedCornerShape(40.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "MINI FARMER",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chat Messages
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for ((message, isUser) in messages.value) {
                ChatBubble(message = message, isUser = isUser)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Message Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val messageText = remember { mutableStateOf("") }
            BasicTextField(
                value = messageText.value,
                onValueChange = { messageText.value = it },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val userMessage = messageText.value
                    if (userMessage.isNotEmpty()) {
                        messages.value = messages.value + Pair(userMessage, true)
                        messages.value = messages.value + Pair("Hello, and thank you for reaching out to our store. We kindly inform you that we are available to respond to messages on weekdays only, between 9:00 AM and 10:00 PM. Your patience is greatly appreciated as we work to assist you.", false)
                        messageText.value = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(text = "Send", color = Color.White)
            }
        }
    }
}

@Composable
fun ChatBubble(message: String, isUser: Boolean) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isUser) Color(0xFFE1FFC7) else Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        }
    }
}

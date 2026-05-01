package com.example.farmlink_project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomepageScreen(navController: NavController) {
    // Set up the Scaffold with a BottomNavigation
    Scaffold(
        bottomBar = {
            CustomBottomNavigationBarHomepage(navController = navController)
        }
    ) { paddingValues ->
        // Main content of the screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // App Name
            Text(
                text = "FarmLink",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color(0xFF4CAF50)
                ),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main image
            Image(
                painter = painterResource(id = R.drawable.placeholder_image), // Replace with your image
                contentDescription = "FarmLink Picture",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CATEGORIES",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50), shape = RoundedCornerShape(4.dp))
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Categories Row
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                CategoryButton("Vegetables", R.drawable.vegetables, navController)
                CategoryButton("Dairy", R.drawable.dairy, navController)
                CategoryButton("Meat", R.drawable.meat, navController)
                CategoryButton("Fruits", R.drawable.fruits, navController)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Popular Products section
            Text(
                text = "POPULAR PRODUCTS",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50), shape = RoundedCornerShape(4.dp))
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Products Row
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                ProductButton("Carrot", "RM 20.00", "From Kedah", R.drawable.apples, navController)
                ProductButton("Tomato Cherry", "RM 10.00", "From Terengganu", R.drawable.tomatoes, navController)
                ProductButton("Bayam", "RM 5.00", "From Selangor", R.drawable.cabbage, navController)
            }
        }
    }
}

@Composable
fun CategoryButton(text: String, imageRes: Int, navController: NavController) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = text,
            modifier = Modifier
                .size(90.dp)
                .clickable {
                    // Navigate to marketplace screen with the category
                    navController.navigate("marketplace_screen?category=$text")
                }
        )
        Text(text = text, fontSize = 18.sp)
    }
}

@Composable
fun ProductButton(name: String, price: String, location: String, imageRes: Int, navController: NavController) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { navController.navigate("details/${name}") }) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = name,
            modifier = Modifier
                .size(90.dp)
                .padding(bottom = 8.dp)
        )
        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = price, fontSize = 18.sp, color = Color(0xFFFF5722))
        Text(text = location, fontSize = 16.sp, color = Color.Black)
    }
}

@Composable
fun CustomBottomNavigationBarHomepage(navController: NavController) {
    var selectedTab by remember { mutableStateOf("Home") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp) // Adjusted height
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) // Rounded corners at top
            .background(
                color = Color(0xFF006400), // Dark green background
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            NavigationItem("Home", R.drawable.ic_home, selectedTab, navController) { selectedTab = "Home" }
            NavigationItem("Marketplace", R.drawable.ic_marketplace, selectedTab, navController) { selectedTab = "Marketplace" }
            NavigationItem("Profile", R.drawable.ic_profile, selectedTab, navController) { selectedTab = "Profile" }
        }
    }
}

@Composable
fun NavigationItem(
    label: String,
    iconRes: Int,
    selectedTab: String,
    navController: NavController,
    onClick: () -> Unit
) {
    IconButton(onClick = {
        onClick()
        navController.navigate(label.lowercase() + "_screen")
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp) // Added padding around the column
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = if (selectedTab == label) Color.White else Color.LightGray,
                modifier = Modifier.size(30.dp) // Adjust icon size
            )
            Spacer(modifier = Modifier.height(4.dp)) // Reduced space between icon and text
            Text(
                text = label,
                fontSize = 12.sp, // Reduced text size for better balance
                color = if (selectedTab == label) Color.White else Color.LightGray,
            )
        }
    }
}

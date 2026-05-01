package com.example.farmlink_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.farmlink_project.data.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MarketplaceScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

data class MarketplaceItem(val name: String, val price: String, val imageRes: Int)

val items = listOf(
    MarketplaceItem("Red Chili", "RM 2.00", R.drawable.red_chili),
    MarketplaceItem("Cabbage", "RM 5.00", R.drawable.cabbage),
    MarketplaceItem("Apples", "RM 15.00", R.drawable.apples),
    MarketplaceItem("Donut Peach", "RM 15.00", R.drawable.donut_peach),
    MarketplaceItem("Meat", "RM 30", R.drawable.meat),
    MarketplaceItem("Tomatoes", "RM 1.70", R.drawable.tomatoes)
)

@Composable
fun MarketplaceHeader(navigateToCart: () -> Unit, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF006400))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .clickable { navController.popBackStack() }
                .padding(end = 8.dp)
        )
        Text(
            text = "MarketPlace",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_shopping_cart),
            contentDescription = "Cart",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .clickable { navigateToCart() }
        )
    }
}

@Composable
fun EnhancedImageCard(item: MarketplaceItem, location: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .clickable { onClick() }
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.small)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.name, style = MaterialTheme.typography.bodyMedium)
            Text(text = item.price, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            Text(text = "From $location", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun CustomBottomNavigationBarMarketplace(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("Marketplace") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color(0xFF006400)) // Set the background color here
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            NavigationItem("HomepageScreen", R.drawable.ic_home, selectedTab, navController) {
                selectedTab = "Home"
                navController.navigate("home_screen") // Navigate to HomepageScreen
            }
            NavigationItem("Marketplace", R.drawable.ic_marketplace, selectedTab, navController) {
                selectedTab = "Marketplace"
            }
            NavigationItem("Profile", R.drawable.ic_profile, selectedTab, navController) {
                selectedTab = "Profile"
                navController.navigate("profile_screen") // Navigate to ProfileScreen
            }
        }
    }
}

@Composable
fun MarketplaceScreenUpdated(
    navigateToDetails: (String) -> Unit,
    navigateToCart: () -> Unit,
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            MarketplaceHeader(
                navigateToCart = navigateToCart,
                navController = navController
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.weight(1f) // Ensures the grid takes up remaining space
            ) {
                items(items) { item ->
                    val location = when (item.name) {
                        "Red Chili" -> "Labuan"
                        "Carrot" -> "Selangor"
                        "Meat" -> "Perak"
                        "Donut Peach" -> "Cameron Highland"
                        "Cabbage" -> "Cameron Highland"
                        "Apples" -> "Cameron Highland"
                        "Tomatoes" -> "Perak"
                        else -> "Unknown"
                    }
                    EnhancedImageCard(
                        item = item,
                        location = location,
                        onClick = { navigateToDetails(item.name) }
                    )
                }
            }
        }
        CustomBottomNavigationBarMarketplace(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter) // Place at the bottom
        )
    }
}

@Composable
fun MarketplaceScreen(navigateToDetails: (String) -> Unit, navigateToCart: () -> Unit) {
    val navController = rememberNavController()
    MarketplaceScreenUpdated(
        navigateToDetails = navigateToDetails,
        navigateToCart = navigateToCart,
        navController = navController
    )
}

@Composable
fun DetailsScreen(
    itemName: String,
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit,
    navController: NavController
) {
    val item = items.find { it.name == itemName }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    var expandedDescription by remember { mutableStateOf(false) }
    var expandedFarmerInfo by remember { mutableStateOf(false) }

    // Function to add item to cart in Firebase
    fun addToCart(item: MarketplaceItem) {
        val cartItem = hashMapOf(
            "name" to item.name,
            "price" to item.price,
            "imageRes" to item.imageRes,
            "timestamp" to System.currentTimeMillis(),
            "userId" to FirebaseAuth.getInstance().currentUser?.uid
        )

        db.collection("shopping_cart")
            .add(cartItem)
            .addOnSuccessListener {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Your item '${item.name}' has been added to the cart!")
                }
            }
            .addOnFailureListener { e ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Failed to add item to cart: ${e.message}")
                }
            }
    }

    if (item != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8E1D4)) // Nude Background Color
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable { navigateBack() }
                            .size(24.dp)
                            .padding(end = 8.dp),
                        tint = Color.Black
                    )
                    Text(
                        text = "Details",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_shopping_cart),
                    contentDescription = "Shopping Basket",
                    modifier = Modifier
                        .clickable { navigateToCart() }
                        .size(24.dp),
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Image
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Item Details
            Text(
                text = item.price,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .clickable { expandedDescription = !expandedDescription }
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = if (expandedDescription) R.drawable.star else R.drawable.star),
                        contentDescription = "Toggle Description",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                AnimatedVisibility(visible = expandedDescription) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .animateContentSize()
                    ) {
                        Text(
                            text = "These ingredients are high in water content and often form the base for many dishes, providing moisture, flavor, and nutritional value.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Farmer Details
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.navigate("profile_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)), // Dark Green Color
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f) // Ensure the buttons are the same size
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "Farmer",
                            tint = Color.White, // Icon color to contrast with dark green
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Mini Farmer",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Text(
                                text = "ONLINE",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Green
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { navController.navigate("chat_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)), // Dark Green Color
                    modifier = Modifier
                        .weight(1f) // Same size as the Mini Farmer button
                ) {
                    Text(text = "Chat Seller", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Add to Cart Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        addToCart(item)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)), // Dark Green Color
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "ADD TO CART", color = Color.White)
                }
            }
        }
    }

    // Display Snackbar
    SnackbarHost(hostState = snackbarHostState)
}


@Composable
fun CartScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cart Screen",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black
        )
    }
}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "marketplace_screen") {
        composable("marketplace_screen") {
            MarketplaceScreen(
                navigateToDetails = { itemName ->
                    navController.navigate("details_screen/$itemName")
                },
                navigateToCart = { navController.navigate("cart_screen") }
            )
        }
        composable(
            "details_screen/{itemName}",
            arguments = listOf(navArgument("itemName") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemName = backStackEntry.arguments?.getString("itemName") ?: ""
            DetailsScreen(
                itemName = itemName,
                navigateBack = { navController.popBackStack() },
                navigateToCart = { navController.navigate("cart_screen") },
                navController = navController
            )
        }
        composable("cart_screen") {
            CartScreen()
        }
        composable("orderlist_screen") {
            OrderListScreen(navController = navController)
        }
        composable("profile_screen") {
            ProfileScreen(navController = navController)
        }
        composable("chat_screen") {
            ChatScreen()
        }
    }
}

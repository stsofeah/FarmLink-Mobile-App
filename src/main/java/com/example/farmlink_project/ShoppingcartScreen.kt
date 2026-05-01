package com.example.farmlink_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// Define theme colors
private val DarkBrown = Color(0xFF5D4037)
private val Nude = Color(0xFFD7CCC8)
private val DarkGreen = Color(0xFF2E7D32)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = DarkGreen,
            onPrimary = DarkBrown,
            background = Nude,
            surface = Nude,
            onSurface = DarkBrown
        ),
        typography = Typography(
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(color = DarkBrown)
        )
    ) {
        content()
    }
}

data class CartItem(
    val id: String,
    val name: String,
    val price: String,
    val imageRes: Int,
    val timestamp: Long
)

class ShoppingCartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "shopping_cart") {
                    composable("shopping_cart") {
                        ShoppingCartScreen(navController = navController)
                    }
                    composable("order_list") {
                        OrderListScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingCartScreen(navController: NavController) {
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

    // Fetch cart items from Firebase
    LaunchedEffect(key1 = true) {
        db.collection("shopping_cart")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    cartItems = snapshot.documents.mapNotNull { doc ->
                        CartItem(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            price = doc.getString("price") ?: "",
                            imageRes = doc.getLong("imageRes")?.toInt() ?: R.drawable.tomatoes,
                            timestamp = doc.getLong("timestamp") ?: 0L
                        )
                    }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Nude)
    ) {
        ShoppingCartHeader(
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Your cart is empty", color = DarkBrown)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(item = item, onRemove = {
                            db.collection("shopping_cart").document(item.id).delete()
                        })
                    }
                }

                // Total section
                val total = cartItems.sumOf {
                    it.price.replace("RM ", "").toDoubleOrNull() ?: 0.0
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Nude)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", color = DarkBrown)
                        Text("RM %.2f".format(total), color = DarkGreen)
                    }
                }

                Button(
                    onClick = {
                        navController.navigate("order_list")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen, contentColor = Nude)
                ) {
                    Text("Proceed to Checkout")
                }
            }
        }
    }
}

@Composable
fun ShoppingCartHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            modifier = Modifier
                .clickable { onBackClick() }
                .size(24.dp),
            tint = DarkBrown
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Shopping Cart",
            color = DarkBrown,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CartItemCard(item: CartItem, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Nude)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier.size(60.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = item.name, color = DarkBrown)
                Text(text = item.price, color = DarkBrown)
            }

            IconButton(onClick = onRemove) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Remove item",
                    tint = DarkBrown
                )
            }
        }
    }
}

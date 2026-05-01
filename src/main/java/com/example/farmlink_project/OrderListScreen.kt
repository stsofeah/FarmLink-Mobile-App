package com.example.farmlink_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.farmlink_project.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import android.app.Activity
import androidx.compose.ui.platform.LocalContext

class OrderListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            OrderListScreen(navController = navController)
        }
    }
}

@Composable
fun OrderListScreen(navController: NavController) {
    var showReceipt by remember { mutableStateOf(false) }
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()
    
    // Remember selected payment and shipping methods
    var selectedShippingOption by remember { mutableStateOf("Select Option") }
    var selectedPaymentMethod by remember { mutableStateOf("Select Payment Method") }

    // Fetch cart items from Firebase
    LaunchedEffect(key1 = true) {
        db.collection("shopping_cart")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                cartItems = documents.mapNotNull { doc ->
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

    if (showReceipt) {
        val totalAmount = cartItems.sumOf { 
            it.price.replace("RM ", "").toDoubleOrNull() ?: 0.0 
        } + 5.90

        ReceiptScreen(
            navController = navController,
            cartItems = cartItems,
            totalAmount = totalAmount
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F4E3))
                .padding(16.dp)
        ) {
            TopBar(navController = navController)
            CartItemsList(cartItems = cartItems)
            ShippingAndPaymentSection(
                onShippingMethodSelected = { selectedShippingOption = it },
                onPaymentMethodSelected = { selectedPaymentMethod = it }
            )
            PaymentDetailsSection(cartItems = cartItems)
            PlaceOrderButton(
                onPlaceOrder = { 
                    // Clear cart in Firebase
                    cartItems.forEach { item ->
                        db.collection("shopping_cart").document(item.id).delete()
                    }
                    showReceipt = true 
                }
            )
        }
    }
}

@Composable
fun ReceiptScreen(
    navController: NavController,
    cartItems: List<CartItem>,
    totalAmount: Double
) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Create receipt text with order details
    val receiptText = remember(cartItems) {
        buildString {
            appendLine("🧾 FarmLink Purchase Receipt")
            appendLine("============================")
            appendLine("Order Details:")
            appendLine()
            cartItems.forEach { item ->
                appendLine("${item.name}: ${item.price}")
            }
            appendLine()
            appendLine("Shipping: RM 5.90")
            appendLine("Total Amount: RM %.2f".format(totalAmount))
            appendLine()
            appendLine("Thank you for shopping with FarmLink! 🌾")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9EAD3))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Receipt",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF274E13)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Thank you for your purchase! 🛍️",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6D4C41)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { 
                    // Navigate to marketplace and clear the back stack
                    navController.navigate("home_screen") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Back to Marketplace", color = Color.White)
            }

            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "My FarmLink Purchase Receipt")
                        putExtra(Intent.EXTRA_TEXT, receiptText)
                    }
                    activity?.startActivity(Intent.createChooser(shareIntent, "Share Receipt"))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                    Text("Share Receipt", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Order List",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CartItemsList(cartItems: List<CartItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        cartItems.forEach { item ->
            ProductItem(
                name = item.name,
                quantity = "1KG", // You might want to add quantity to your CartItem data class
                price = item.price,
                seller = "From Marketplace" // You might want to add seller info to your CartItem
            )
        }
    }
}

@Composable
fun ProductItem(name: String, quantity: String, price: String, seller: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.apples),
            contentDescription = "Product Image",
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "$name $quantity", fontWeight = FontWeight.Bold)
            Text(text = seller, style = MaterialTheme.typography.bodyLarge)
        }
        Text(text = price, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ShippingAndPaymentSection(
    onShippingMethodSelected: (String) -> Unit,
    onPaymentMethodSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Shipping Option", fontWeight = FontWeight.Bold)

        // State to manage the dropdown for Shipping Option
        var shippingExpanded by remember { mutableStateOf(false) }
        var selectedShippingOption by remember { mutableStateOf("Select Option") }
        val shippingOptions = listOf("COD", "Shipping")

        // Combobox alternative for Shipping Option
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { shippingExpanded = !shippingExpanded },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = selectedShippingOption)
            }

            if (shippingExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    shippingOptions.forEach { option ->
                        TextButton(
                            onClick = {
                                selectedShippingOption = option
                                shippingExpanded = false
                                onShippingMethodSelected(option)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = option)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment Methods Combobox
        Text(text = "Payment Methods", fontWeight = FontWeight.Bold)

        var paymentExpanded by remember { mutableStateOf(false) }
        var selectedPaymentMethod by remember { mutableStateOf("Select Payment Method") }
        val paymentMethods = listOf("Online Banking", "Cash")

        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { paymentExpanded = !paymentExpanded },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = selectedPaymentMethod)
            }

            if (paymentExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    paymentMethods.forEach { method ->
                        TextButton(
                            onClick = {
                                selectedPaymentMethod = method
                                paymentExpanded = false
                                onPaymentMethodSelected(method)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = method)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentDetailsSection(cartItems: List<CartItem>) {
    val merchandiseTotal = cartItems.sumOf { 
        it.price.replace("RM ", "").toDoubleOrNull() ?: 0.0 
    }
    val shippingCost = 5.90
    val totalPayment = merchandiseTotal + shippingCost

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFFFF4CC), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = "Payment Details", fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Merchandise Subtotal")
            Text(text = "RM %.2f".format(merchandiseTotal))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Shipping Subtotal (excl. sst)")
            Text(text = "RM %.2f".format(shippingCost))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Total Payment", fontWeight = FontWeight.Bold)
            Text(text = "RM %.2f".format(totalPayment), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PlaceOrderButton(onPlaceOrder: () -> Unit) {
    Button(
        onClick = { onPlaceOrder() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
    ) {
        Text(text = "PLACE ORDER", color = Color.White, fontWeight = FontWeight.Bold)
    }
}


package com.example.farmlink_project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import com.example.farmlink_project.data.ProductEntity
import com.example.farmlink_project.data.ProductRepository

@Composable
fun ProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var stockQuantity by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { ProductRepository(context) }
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Define deleteProduct function here, before it's used
    fun deleteProduct(product: ProductEntity) {
        scope.launch {
            try {
                repository.deleteProduct(product)
                // You could show a snackbar or toast here to confirm deletion
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error
            }
        }
    }

    if (currentUser == null) {
        Text(
            text = "Please log in to manage products",
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F4E3))
    ) {
        // TopBar (Keep outside scrollable area)
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
                text = "Profile",
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            
            Icon(
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = "Profile Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Profile Information
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ROOTLY",
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Lot 888, Jalan Emas, Taman Hartamas, KL.",
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stocks Section Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2E5A1C)
                )
            ) {
                Text(
                    text = "Your Listed Products",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Listed Products
            val products by remember(currentUser.uid) {
                repository.getProductsBySeller(currentUser.uid)
            }.collectAsState(initial = emptyList())

            if (products.isNotEmpty()) {
                products.forEach { product ->
                    ProductItem(
                        product = product,
                        onDelete = { deleteProduct(product) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F0E1)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "No products",
                            tint = Color(0xFF6B4423),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No products listed yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF6B4423)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add New Products Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2E5A1C)
                )
            ) {
                Text(
                    text = "Add New Product",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Product Form
            Column {
                val textFieldModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") },
                    modifier = textFieldModifier
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight") },
                    modifier = textFieldModifier
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = textFieldModifier
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = textFieldModifier
                )
                OutlinedTextField(
                    value = stockQuantity,
                    onValueChange = { stockQuantity = it },
                    label = { Text("Stock Quantity") },
                    modifier = textFieldModifier
                )
            }

            Button(
                onClick = {
                    if (name.isBlank() || weight.isBlank() || price.isBlank() || 
                        location.isBlank() || stockQuantity.isBlank()) {
                        return@Button
                    }
                    
                    scope.launch {
                        try {
                            repository.addProduct(
                                name = name,
                                weight = weight,
                                price = price,
                                location = location,
                                stockQuantity = stockQuantity.toIntOrNull() ?: 0,
                                sellerId = currentUser.uid
                            )
                            name = ""
                            weight = ""
                            price = ""
                            location = ""
                            stockQuantity = ""
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E5A1C)
                )
            ) {
                Text("LIST NOW", color = Color.White)
            }

            // Add some padding at the bottom
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProductItem(
    product: ProductEntity,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F0E1)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF2E5A1C),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = "Location",
                            tint = Color(0xFF6B4423),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = product.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF6B4423)
                        )
                    }
                }
                Text(
                    text = "RM ${product.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF2E5A1C),
                    fontWeight = FontWeight.Bold
                )
                
                // Add Delete Button
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(36.dp)
                ) {
                    Text(
                        "Delete",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFD4C5B9))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProductDetailChip(
                    icon = R.drawable.ic_person,
                    label = "Weight",
                    value = product.weight
                )
                ProductDetailChip(
                    icon = R.drawable.ic_person,
                    label = "Stock",
                    value = "${product.stockQuantity} units"
                )
            }
        }
    }
}

@Composable
fun ProductDetailChip(
    icon: Int,
    label: String,
    value: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFE8F5E9)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = Color(0xFF2E5A1C),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B4423)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2E5A1C),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
// ui/screens/ProfileScreen.kt
package com.example.thecodecup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.thecodecup.R
import com.example.thecodecup.data.repository.UserPreferencesRepository
import com.example.thecodecup.ui.components.EditProfileDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userRepository = remember { UserPreferencesRepository(context) }
    val userProfile by userRepository.userProfile.collectAsStateWithLifecycle()

    var showEditDialog by remember { mutableStateOf(false) }
    var editingField by remember { mutableStateOf("") }
    var editingTitle by remember { mutableStateOf("") }
    var editingValue by remember { mutableStateOf("") }
    var isMultiLine by remember { mutableStateOf(false) }
    var keyboardType by remember { mutableStateOf(KeyboardType.Text) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with Navigation
        TopAppBar(
            title = {
                Text(
                    text = "Profile",
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Information Cards
            ProfileInfoCard(
                icon = R.drawable.ic_profile_,
                label = "Full name",
                value = userProfile.fullName,
                showEdit = true,
                onEdit = {
                    editingField = "full_name"
                    editingTitle = "Full name"
                    editingValue = userProfile.fullName
                    isMultiLine = false
                    keyboardType = KeyboardType.Text
                    showEditDialog = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoCard(
                icon = R.drawable.ic_call,
                label = "Phone number",
                value = userProfile.phoneNumber,
                showEdit = true,
                onEdit = {
                    editingField = "phone_number"
                    editingTitle = "Phone number"
                    editingValue = userProfile.phoneNumber
                    isMultiLine = false
                    keyboardType = KeyboardType.Phone
                    showEditDialog = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoCard(
                icon = R.drawable.ic_message,
                label = "Email",
                value = userProfile.email,
                showEdit = true,
                onEdit = {
                    editingField = "email"
                    editingTitle = "Email"
                    editingValue = userProfile.email
                    isMultiLine = false
                    keyboardType = KeyboardType.Email
                    showEditDialog = true
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoCard(
                icon = R.drawable.ic_location,
                label = "Address",
                value = userProfile.address,
                showEdit = true,
                isMultiLine = true,
                onEdit = {
                    editingField = "address"
                    editingTitle = "Address"
                    editingValue = userProfile.address
                    isMultiLine = true
                    keyboardType = KeyboardType.Text
                    showEditDialog = true
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Edit Dialog
    EditProfileDialog(
        isVisible = showEditDialog,
        title = editingTitle,
        currentValue = editingValue,
        isMultiLine = isMultiLine,
        keyboardType = keyboardType,
        onDismiss = { showEditDialog = false },
        onSave = { newValue ->
            userRepository.updateField(editingField, newValue)
        }
    )
}

@Composable
fun ProfileInfoCard(
    icon: Int,
    label: String,
    value: String,
    showEdit: Boolean = false,
    isMultiLine: Boolean = false,
    onEdit: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = if (isMultiLine) Alignment.Top else Alignment.CenterVertically
        ) {
            // Icon Section
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = label,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content Section
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Label
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Value
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    lineHeight = if (isMultiLine) 20.sp else 16.sp
                )
            }

            // Edit Icon
            if (showEdit) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "Edit $label",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}
package com.example.thecodecup.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.thecodecup.R
import com.example.thecodecup.data.repository.UserPreferencesRepository
import com.example.thecodecup.ui.components.EditProfileDialog
import com.example.thecodecup.ui.components.ThemeToggleButton
import com.example.thecodecup.ui.theme.CoffeeBrown
import com.example.thecodecup.ui.theme.LocalThemeManager

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

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { userRepository.updateProfileImage(it.toString()) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with Navigation and Theme Toggle
        TopAppBar(
            title = {
                Text(
                    text = "Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                ThemeToggleButton()
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Avatar Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    // Avatar Image
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (userProfile.profileImageUri != null) {
                            AsyncImage(
                                model = userProfile.profileImageUri,
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Avatar",
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Camera Icon Button
                    FloatingActionButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.size(36.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Avatar",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // User Name Display
            Text(
                text = userProfile.fullName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = userProfile.email,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

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
    if (showEditDialog) {
        EditProfileDialog(
            isVisible = showEditDialog,
            title = editingTitle,
            currentValue = editingValue,
            isMultiLine = isMultiLine,
            keyboardType = keyboardType,
            onDismiss = { showEditDialog = false },
            onSave = { newValue ->
                userRepository.updateField(editingField, newValue)
                showEditDialog = false
            }
        )
    }
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
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = if (isMultiLine) Alignment.Top else Alignment.CenterVertically
        ) {
            // Icon Section
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = label,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Value
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    lineHeight = if (isMultiLine) 20.sp else 16.sp
                )
            }

            // Edit Icon
            if (showEdit) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "Edit $label",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
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
package com.example.signalbind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.signalbind.ui.viewmodel.KycViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KycFormScreen(
    onNavigateToResult: () -> Unit,
    viewModel: KycViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var showDatePicker by remember { mutableStateOf(false) }
    
    // Navigate to result screen when KYC submission is successful
    LaunchedEffect(uiState.kycResponse) {
        if (uiState.kycResponse != null) {
            onNavigateToResult()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        // Header
        Text(
            text = "KYC Identity Verification",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = "Please provide your identity information for verification",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Divider()
        
        // Required Fields Section
        Text(
            text = "Required Information",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        // Phone Number
        KycTextField(
            value = uiState.phoneNumber,
            onValueChange = viewModel::updatePhoneNumber,
            label = "Phone Number",
            placeholder = "Enter your phone number",
            keyboardType = KeyboardType.Phone,
            isRequired = true,
            errorMessage = uiState.fieldErrors["phoneNumber"]
        )
        
        // ID Document
        KycTextField(
            value = uiState.idDocument,
            onValueChange = viewModel::updateIdDocument,
            label = "ID Document Number",
            placeholder = "Enter your ID document number",
            isRequired = true,
            errorMessage = uiState.fieldErrors["idDocument"]
        )
        
        // Name
        KycTextField(
            value = uiState.name,
            onValueChange = viewModel::updateName,
            label = "Full Name",
            placeholder = "Enter your full name",
            isRequired = true,
            errorMessage = uiState.fieldErrors["name"]
        )
        
        // Given Name
        KycTextField(
            value = uiState.givenName,
            onValueChange = viewModel::updateGivenName,
            label = "Given Name (First Name)",
            placeholder = "Enter your first name",
            isRequired = true,
            errorMessage = uiState.fieldErrors["givenName"]
        )
        
        // Family Name
        KycTextField(
            value = uiState.familyName,
            onValueChange = viewModel::updateFamilyName,
            label = "Family Name (Last Name)",
            placeholder = "Enter your last name",
            isRequired = true,
            errorMessage = uiState.fieldErrors["familyName"]
        )
        
        // Birthdate
        OutlinedTextField(
            value = uiState.birthdate,
            onValueChange = { },
            label = { Text("Birthdate *") },
            placeholder = { Text("YYYY-MM-DD") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select date")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.fieldErrors["birthdate"] != null,
            supportingText = uiState.fieldErrors["birthdate"]?.let { { Text(it) } }
        )
        
        // Gender Dropdown
        var genderExpanded by remember { mutableStateOf(false) }
        val genderOptions = listOf("Male", "Female", "Other")
        
        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.gender,
                onValueChange = { },
                label = { Text("Gender *") },
                placeholder = { Text("Select your gender") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                isError = uiState.fieldErrors["gender"] != null,
                supportingText = uiState.fieldErrors["gender"]?.let { { Text(it) } }
            )
            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                genderOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            viewModel.updateGender(option)
                            genderExpanded = false
                        }
                    )
                }
            }
        }
        
        // Email
        KycTextField(
            value = uiState.email,
            onValueChange = viewModel::updateEmail,
            label = "Email Address",
            placeholder = "Enter your email address",
            keyboardType = KeyboardType.Email,
            isRequired = true,
            errorMessage = uiState.fieldErrors["email"]
        )
        
        Divider()
        
        // Optional Fields Section
        Text(
            text = "Additional Information (Optional)",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        // Middle Names
        KycTextField(
            value = uiState.middleNames,
            onValueChange = viewModel::updateMiddleNames,
            label = "Middle Names",
            placeholder = "Enter your middle names"
        )
        
        // Family Name at Birth
        KycTextField(
            value = uiState.familyNameAtBirth,
            onValueChange = viewModel::updateFamilyNameAtBirth,
            label = "Family Name at Birth",
            placeholder = "Enter your family name at birth"
        )
        
        // Name Kana (Hankaku)
        KycTextField(
            value = uiState.nameKanaHankaku,
            onValueChange = viewModel::updateNameKanaHankaku,
            label = "Name Kana (Hankaku)",
            placeholder = "Enter name in half-width katakana"
        )
        
        // Name Kana (Zenkaku)
        KycTextField(
            value = uiState.nameKanaZenkaku,
            onValueChange = viewModel::updateNameKanaZenkaku,
            label = "Name Kana (Zenkaku)",
            placeholder = "Enter name in full-width katakana"
        )
        
        Divider()
        
        // Address Section
        Text(
            text = "Address Information (Optional)",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        // Address
        KycTextField(
            value = uiState.address,
            onValueChange = viewModel::updateAddress,
            label = "Address",
            placeholder = "Enter your full address"
        )
        
        // Street Name
        KycTextField(
            value = uiState.streetName,
            onValueChange = viewModel::updateStreetName,
            label = "Street Name",
            placeholder = "Enter street name"
        )
        
        // Street Number
        KycTextField(
            value = uiState.streetNumber,
            onValueChange = viewModel::updateStreetNumber,
            label = "Street Number",
            placeholder = "Enter street number"
        )
        
        // House Number Extension
        KycTextField(
            value = uiState.houseNumberExtension,
            onValueChange = viewModel::updateHouseNumberExtension,
            label = "House Number Extension",
            placeholder = "Enter house number extension"
        )
        
        // Postal Code
        KycTextField(
            value = uiState.postalCode,
            onValueChange = viewModel::updatePostalCode,
            label = "Postal Code",
            placeholder = "Enter postal code"
        )
        
        // Region
        KycTextField(
            value = uiState.region,
            onValueChange = viewModel::updateRegion,
            label = "Region/State",
            placeholder = "Enter region or state"
        )
        
        // Locality
        KycTextField(
            value = uiState.locality,
            onValueChange = viewModel::updateLocality,
            label = "Locality/City",
            placeholder = "Enter locality or city"
        )
        
        // Country
        KycTextField(
            value = uiState.country,
            onValueChange = viewModel::updateCountry,
            label = "Country",
            placeholder = "Enter country"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Submit Button
        Button(
            onClick = viewModel::submitKycForm,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Submitting...")
            } else {
                Text("Submit KYC")
            }
        }
        
        // Error Message
        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
    
    // Simple date input dialog
    if (showDatePicker) {
        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            title = { Text("Enter Birthdate") },
            text = {
                Column {
                    Text("Please enter your birthdate in YYYY-MM-DD format")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.birthdate,
                        onValueChange = { date ->
                            viewModel.updateBirthdate(Calendar.getInstance().apply {
                                try {
                                    val parts = date.split("-")
                                    if (parts.size == 3) {
                                        set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
                                    }
                                } catch (e: Exception) {
                                    // Invalid date format
                                }
                            }.time)
                        },
                        label = { Text("YYYY-MM-DD") },
                        placeholder = { Text("1990-01-01") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KycTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isRequired: Boolean = false,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(if (isRequired) "$label *" else label) },
        placeholder = { Text(placeholder) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = errorMessage != null,
        supportingText = errorMessage?.let { { Text(it) } }
    )
}

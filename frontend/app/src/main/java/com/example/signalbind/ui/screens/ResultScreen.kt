package com.example.signalbind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.signalbind.ui.viewmodel.VerificationViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    onNavigateBack: () -> Unit,
    viewModel: VerificationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val consentReceipt = uiState.consentReceipt
    
    if (consentReceipt == null) {
        // If no receipt, navigate back to home
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        // Header
        Text(
            text = "Verification Result",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (consentReceipt.verificationStatus) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.errorContainer
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (consentReceipt.verificationStatus) {
                        Icons.Default.CheckCircle
                    } else {
                        Icons.Default.Close
                    },
                    contentDescription = "Status",
                    modifier = Modifier.size(48.dp),
                    tint = if (consentReceipt.verificationStatus) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (consentReceipt.verificationStatus) {
                        "Verification Successful"
                    } else {
                        "Verification Failed"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (consentReceipt.verificationStatus) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            }
        }
        
        // Receipt Details Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Consent Receipt Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Divider()
                
                // Receipt ID
                ReceiptDetailRow(
                    label = "Receipt ID",
                    value = consentReceipt.receiptId
                )
                
                // Verification Status
                ReceiptDetailRow(
                    label = "Verification Status",
                    value = if (consentReceipt.verificationStatus) "Verified" else "Not Verified"
                )
                
                // Recycled Since
                consentReceipt.recycledSince?.let { recycledSince ->
                    ReceiptDetailRow(
                        label = "Recycled Since",
                        value = formatDate(recycledSince)
                    )
                }
                
                // Reason Code
                consentReceipt.reasonCode?.let { reasonCode ->
                    ReceiptDetailRow(
                        label = "Reason Code",
                        value = reasonCode
                    )
                }
                
                // Timestamp
                ReceiptDetailRow(
                    label = "Timestamp",
                    value = formatDate(consentReceipt.timestamp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Back Button
        Button(
            onClick = {
                viewModel.clearReceipt()
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify Another Number")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ReceiptDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun formatDate(dateString: String): String {
    return try {
        // Try to parse ISO format first
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        // If parsing fails, return the original string
        dateString
    }
}

package com.example.signalbind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
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
import com.example.signalbind.ui.viewmodel.KycViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KycResultScreen(
    onNavigateBack: () -> Unit,
    viewModel: KycViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val kycResponse = uiState.kycResponse
    
    if (kycResponse == null) {
        // If no response, navigate back to form
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
            text = "KYC Verification Result",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Match Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (kycResponse.matchResult) {
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
                    imageVector = if (kycResponse.matchResult) {
                        Icons.Default.CheckCircle
                    } else {
                        Icons.Default.Close
                    },
                    contentDescription = "Match Result",
                    modifier = Modifier.size(64.dp),
                    tint = if (kycResponse.matchResult) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (kycResponse.matchResult) {
                        "Identity Verified"
                    } else {
                        "Identity Not Verified"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (kycResponse.matchResult) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                )
                
                Text(
                    text = if (kycResponse.matchResult) {
                        "Your identity has been successfully verified"
                    } else {
                        "Your identity could not be verified. Please check your information and try again."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = if (kycResponse.matchResult) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            }
        }
        
        // Confidence Score
        kycResponse.confidenceScore?.let { score ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Confidence Score",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = "${(score * 100).toInt()}%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    LinearProgressIndicator(
                        progress = score.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
        
        // Match Details
        kycResponse.matchDetails?.let { details ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Verification Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Divider()
                    
                    // Name Match
                    details.nameMatch?.let { match ->
                        MatchDetailRow(
                            label = "Name Match",
                            isMatch = match
                        )
                    }
                    
                    // Document Match
                    details.documentMatch?.let { match ->
                        MatchDetailRow(
                            label = "Document Match",
                            isMatch = match
                        )
                    }
                    
                    // Address Match
                    details.addressMatch?.let { match ->
                        MatchDetailRow(
                            label = "Address Match",
                            isMatch = match
                        )
                    }
                    
                    // Phone Match
                    details.phoneMatch?.let { match ->
                        MatchDetailRow(
                            label = "Phone Match",
                            isMatch = match
                        )
                    }
                    
                    // Email Match
                    details.emailMatch?.let { match ->
                        MatchDetailRow(
                            label = "Email Match",
                            isMatch = match
                        )
                    }
                }
            }
        }
        
        // Request Information
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Request Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Divider()
                
                // Request ID
                kycResponse.requestId?.let { requestId ->
                    KycResultDetailRow(
                        label = "Request ID",
                        value = requestId
                    )
                }
                
                // Timestamp
                KycResultDetailRow(
                    label = "Timestamp",
                    value = formatTimestamp(kycResponse.timestamp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.clearResponse()
                    onNavigateBack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Submit Another")
            }
            
            Button(
                onClick = {
                    viewModel.clearResponse()
                    onNavigateBack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Done")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun MatchDetailRow(
    label: String,
    isMatch: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if (isMatch) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = if (isMatch) "Match" else "No Match",
                modifier = Modifier.size(20.dp),
                tint = if (isMatch) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
            
            Text(
                text = if (isMatch) "Match" else "No Match",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isMatch) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun KycResultDetailRow(
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

private fun formatTimestamp(timestamp: String): String {
    return try {
        // Try to parse ISO format first
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(timestamp)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        // If parsing fails, return the original string
        timestamp
    }
}

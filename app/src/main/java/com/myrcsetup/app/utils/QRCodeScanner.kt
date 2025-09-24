package com.myrcsetup.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

/**
 * Utilitaire pour scanner des QR codes avec gestion des permissions
 */
object QRCodeScanner {
    
    /**
     * Vérifie si la permission caméra est accordée
     */
    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Crée les options de scan optimisées pour les QR codes
     */
    fun createScanOptions(): ScanOptions {
        return ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scannez le QR code de la session")
            setCameraId(0) // Caméra arrière
            setBeepEnabled(true)
            setBarcodeImageEnabled(false)
            setOrientationLocked(true) // Verrouiller en mode Portrait
            setTimeout(30000) // 30 secondes timeout
        }
    }
}

/**
 * Composable pour scanner un QR code avec gestion des permissions
 * 
 * @param onResult Callback appelé avec le résultat du scan
 * @param onPermissionDenied Callback appelé si la permission est refusée
 * @param modifier Modifier pour le composable
 */
@Composable
fun QRCodeScannerComposable(
    onResult: (String?) -> Unit,
    onPermissionDenied: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    var hasPermission by remember { 
        mutableStateOf(QRCodeScanner.hasCameraPermission(context)) 
    }
    
    // Launcher pour demander la permission caméra
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (!isGranted) {
            showPermissionDialog = true
        }
    }
    
    // Launcher pour le scan de QR code
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            onResult(null) // Scan annulé
        } else {
            onResult(result.contents) // Scan réussi
        }
    }
    
    // Fonction pour démarrer le scan
    val startScan = {
        if (hasPermission) {
            val options = QRCodeScanner.createScanOptions()
            scanLauncher.launch(options)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    
    // Interface utilisateur
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!hasPermission) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Permission caméra requise",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "L'accès à la caméra est nécessaire pour scanner les QR codes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        Button(
            onClick = startScan,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (hasPermission) "Scanner QR Code" else "Autoriser et Scanner"
            )
        }
    }
    
    // Dialogue d'information sur la permission refusée
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { 
                showPermissionDialog = false
                onPermissionDenied()
            },
            title = { Text("Permission refusée") },
            text = { 
                Text("La permission caméra est nécessaire pour scanner les QR codes. Vous pouvez l'activer dans les paramètres de l'application.")
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showPermissionDialog = false
                        onPermissionDenied()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

/**
 * Composable simplifié pour un bouton de scan QR
 * 
 * @param onScanResult Callback avec le résultat du scan
 * @param buttonText Texte du bouton
 * @param modifier Modifier pour le bouton
 */
@Composable
fun QRScanButton(
    onScanResult: (String?) -> Unit,
    buttonText: String = "Scanner QR Code",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasPermission by remember { 
        mutableStateOf(QRCodeScanner.hasCameraPermission(context)) 
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (!isGranted) {
            onScanResult(null) // Permission refusée
        }
    }
    
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result: ScanIntentResult ->
        onScanResult(result.contents)
    }
    
    Button(
        onClick = {
            if (hasPermission) {
                val options = QRCodeScanner.createScanOptions()
                scanLauncher.launch(options)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        modifier = modifier
    ) {
        Text(buttonText)
    }
}

/**
 * Fonction utilitaire pour valider le contenu d'un QR code
 * 
 * @param content Contenu du QR code scanné
 * @return true si le contenu semble être un JSON valide de session RC
 */
fun isValidRCSessionQR(content: String?): Boolean {
    if (content.isNullOrBlank()) return false
    
    return try {
        // Vérification basique : doit contenir des champs typiques d'une session RC
        content.contains("carName") && 
        content.contains("trackName") && 
        content.contains("dateTime") &&
        content.startsWith("{") && 
        content.endsWith("}")
    } catch (e: Exception) {
        false
    }
}

/**
 * Fonction pour extraire les informations de base d'un QR code de session
 * 
 * @param content Contenu JSON du QR code
 * @return Pair<carName, trackName> ou null si invalide
 */
fun extractSessionInfo(content: String?): Pair<String, String>? {
    if (!isValidRCSessionQR(content)) return null
    
    return try {
        // Extraction simple des informations de base
        // Note: Pour une extraction complète, il faudrait utiliser kotlinx.serialization
        val carNameRegex = "\"carName\"\\s*:\\s*\"([^\"]+)\"".toRegex()
        val trackNameRegex = "\"trackName\"\\s*:\\s*\"([^\"]+)\"".toRegex()
        
        val carName = carNameRegex.find(content!!)?.groupValues?.get(1) ?: "Session importée"
        val trackName = trackNameRegex.find(content)?.groupValues?.get(1) ?: "Circuit inconnu"
        
        Pair(carName, trackName)
    } catch (e: Exception) {
        null
    }
}
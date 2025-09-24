package com.myrcsetup.app.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * Utilitaire pour générer des QR codes à partir de données JSON
 */
object QRCodeGenerator {
    
    /**
     * Génère un QR code bitmap à partir d'une chaîne de données
     * 
     * @param data Les données à encoder dans le QR code
     * @param width Largeur du QR code en pixels
     * @param height Hauteur du QR code en pixels
     * @return Bitmap du QR code ou null en cas d'erreur
     */
    fun generateQRCode(
        data: String,
        width: Int = 512,
        height: Int = 512
    ): Bitmap? {
        return try {
            val writer = QRCodeWriter()
            
            // Configuration des hints pour optimiser le QR code
            val hints = hashMapOf<EncodeHintType, Any>().apply {
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
                put(EncodeHintType.MARGIN, 1) // Marge minimale
            }
            
            // Génération de la matrice de bits
            val bitMatrix: BitMatrix = writer.encode(
                data,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints
            )
            
            // Conversion en bitmap
            createBitmapFromBitMatrix(bitMatrix)
            
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Convertit une BitMatrix en Bitmap
     */
    private fun createBitmapFromBitMatrix(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x, 
                    y, 
                    if (matrix[x, y]) Color.BLACK else Color.WHITE
                )
            }
        }
        
        return bitmap
    }
    
    /**
     * Génère un QR code avec des couleurs personnalisées
     * 
     * @param data Les données à encoder
     * @param width Largeur du QR code
     * @param height Hauteur du QR code
     * @param foregroundColor Couleur des modules (défaut: noir)
     * @param backgroundColor Couleur de fond (défaut: blanc)
     * @return Bitmap du QR code coloré ou null en cas d'erreur
     */
    fun generateColoredQRCode(
        data: String,
        width: Int = 512,
        height: Int = 512,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Bitmap? {
        return try {
            val writer = QRCodeWriter()
            
            val hints = hashMapOf<EncodeHintType, Any>().apply {
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
                put(EncodeHintType.MARGIN, 1)
            }
            
            val bitMatrix: BitMatrix = writer.encode(
                data,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints
            )
            
            createColoredBitmapFromBitMatrix(bitMatrix, foregroundColor, backgroundColor)
            
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Convertit une BitMatrix en Bitmap avec des couleurs personnalisées
     */
    private fun createColoredBitmapFromBitMatrix(
        matrix: BitMatrix,
        foregroundColor: Int,
        backgroundColor: Int
    ): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x, 
                    y, 
                    if (matrix[x, y]) foregroundColor else backgroundColor
                )
            }
        }
        
        return bitmap
    }
    
    /**
     * Valide si une chaîne peut être encodée en QR code
     * 
     * @param data Les données à valider
     * @return true si les données peuvent être encodées, false sinon
     */
    fun canEncodeData(data: String): Boolean {
        return try {
            if (data.isEmpty()) return false
            
            // Vérification de la taille maximale (approximative)
            // Un QR code peut contenir environ 4296 caractères alphanumériques
            if (data.length > 4000) return false
            
            // Test d'encodage
            val writer = QRCodeWriter()
            writer.encode(data, BarcodeFormat.QR_CODE, 100, 100)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Estime la taille optimale du QR code en fonction de la quantité de données
     * 
     * @param data Les données à encoder
     * @return Taille recommandée en pixels
     */
    fun getOptimalSize(data: String): Int {
        return when {
            data.length <= 100 -> 256
            data.length <= 500 -> 384
            data.length <= 1000 -> 512
            data.length <= 2000 -> 640
            else -> 768
        }
    }
}
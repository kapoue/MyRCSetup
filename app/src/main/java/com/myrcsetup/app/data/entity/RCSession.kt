package com.myrcsetup.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "rc_sessions")
data class RCSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // Informations de base
    val carName: String,
    val trackName: String,
    val dateTime: LocalDateTime,
    val bestLapTime: String, // Format mm:ss.ms
    val comments: String,
    
    // Suspension
    val frontSprings: String,
    val rearSprings: String,
    val frontShockOil: Double?,
    val rearShockOil: Double?,
    val shockPosition: String,
    
    // Différentiels
    val frontDiffOil: Double?,
    val rearDiffOil: Double?,
    val centerDiffOil: Double?,
    
    // Géométrie
    val frontCamber: Double?,
    val rearCamber: Double?,
    val frontToe: String,
    val rearToe: String,
    val caster: Double?,
    
    // Transmission
    val pinion: Int?,
    val spurGear: Int?,
    val finalRatio: Double?, // Calculé automatiquement
    
    // Pneus et adhérence
    val frontTires: String,
    val rearTires: String,
    val tireFoam: String,
    val tractionAdditive: String,
    
    // Réglages châssis
    val chassisStiffness: String,
    val frontRideHeight: Double?,
    val rearRideHeight: Double?,
    val frontAntiRoll: String,
    val rearAntiRoll: String
) {
    // Calcul automatique du rapport de transmission
    fun calculateFinalRatio(): Double? {
        return if (pinion != null && spurGear != null && pinion > 0) {
            spurGear.toDouble() / pinion.toDouble()
        } else null
    }
}
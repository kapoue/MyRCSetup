package com.myrcsetup.app.data.model

import com.myrcsetup.app.data.entity.RCSession
import kotlinx.serialization.Serializable

@Serializable
data class ExportData(
    val version: String = "1.1",
    val appVersion: String = "1.9.6",
    val exportDate: String,
    val sessions: List<SerializableRCSession>,
    val notes: String? = null
)

@Serializable
data class SerializableRCSession(
    val id: Long = 0,
    val carName: String,
    val trackName: String,
    val dateTime: String, // ISO 8601 format
    val bestLapTime: String,
    val comments: String,
    val frontSprings: String,
    val rearSprings: String,
    val frontShockOil: Double?,
    val rearShockOil: Double?,
    val shockPosition: String,
    val frontDiffOil: Double?,
    val rearDiffOil: Double?,
    val centerDiffOil: Double?,
    val frontCamber: Double?,
    val rearCamber: Double?,
    val frontToe: Double?,
    val rearToe: Double?,
    val caster: Double?,
    val pinion: Int?,
    val spurGear: Int?,
    val frontTires: String,
    val rearTires: String,
    val tireFoam: String,
    val tractionAdditive: String,
    val chassisStiffness: String,
    val frontRideHeight: Double?,
    val rearRideHeight: Double?,
    val frontAntiRoll: Double?,
    val rearAntiRoll: Double?
)

// Extension functions pour la conversion
fun RCSession.toSerializable(): SerializableRCSession {
    return SerializableRCSession(
        id = this.id,
        carName = this.carName,
        trackName = this.trackName,
        dateTime = this.dateTime.toString(),
        bestLapTime = this.bestLapTime,
        comments = this.comments,
        frontSprings = this.frontSprings,
        rearSprings = this.rearSprings,
        frontShockOil = this.frontShockOil,
        rearShockOil = this.rearShockOil,
        shockPosition = this.shockPosition,
        frontDiffOil = this.frontDiffOil,
        rearDiffOil = this.rearDiffOil,
        centerDiffOil = this.centerDiffOil,
        frontCamber = this.frontCamber,
        rearCamber = this.rearCamber,
        frontToe = this.frontToe,
        rearToe = this.rearToe,
        caster = this.caster,
        pinion = this.pinion,
        spurGear = this.spurGear,
        frontTires = this.frontTires,
        rearTires = this.rearTires,
        tireFoam = this.tireFoam,
        tractionAdditive = this.tractionAdditive,
        chassisStiffness = this.chassisStiffness,
        frontRideHeight = this.frontRideHeight,
        rearRideHeight = this.rearRideHeight,
        frontAntiRoll = this.frontAntiRoll,
        rearAntiRoll = this.rearAntiRoll
    )
}

fun SerializableRCSession.toRCSession(): RCSession {
    return RCSession(
        id = 0, // Nouvel ID sera généré par Room
        carName = this.carName,
        trackName = this.trackName,
        dateTime = kotlinx.datetime.LocalDateTime.parse(this.dateTime),
        bestLapTime = this.bestLapTime,
        comments = this.comments,
        frontSprings = this.frontSprings,
        rearSprings = this.rearSprings,
        frontShockOil = this.frontShockOil,
        rearShockOil = this.rearShockOil,
        shockPosition = this.shockPosition,
        frontDiffOil = this.frontDiffOil,
        rearDiffOil = this.rearDiffOil,
        centerDiffOil = this.centerDiffOil,
        frontCamber = this.frontCamber,
        rearCamber = this.rearCamber,
        frontToe = this.frontToe,
        rearToe = this.rearToe,
        caster = this.caster,
        pinion = this.pinion,
        spurGear = this.spurGear,
        frontTires = this.frontTires,
        rearTires = this.rearTires,
        tireFoam = this.tireFoam,
        tractionAdditive = this.tractionAdditive,
        chassisStiffness = this.chassisStiffness,
        frontRideHeight = this.frontRideHeight,
        rearRideHeight = this.rearRideHeight,
        frontAntiRoll = this.frontAntiRoll,
        rearAntiRoll = this.rearAntiRoll
    )
}
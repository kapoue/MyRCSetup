package com.myrcsetup.app.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * Gestionnaire des préférences pour les mises à jour
 */
class UpdatePreferences(context: Context) {
    
    companion object {
        private const val TAG = "UpdatePreferences"
        private const val PREFS_NAME = "update_preferences"
        private const val KEY_LAST_CHECK = "last_update_check"
        private const val KEY_LAST_NOTIFIED_VERSION = "last_notified_version"
        private const val CHECK_INTERVAL_DAYS = 3
    }
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    /**
     * Enregistre la date de la dernière vérification
     */
    fun setLastCheckTime(timestamp: Long = System.currentTimeMillis()) {
        prefs.edit().putLong(KEY_LAST_CHECK, timestamp).apply()
        Log.d(TAG, "Dernière vérification enregistrée: $timestamp")
    }
    
    /**
     * Récupère la date de la dernière vérification
     */
    fun getLastCheckTime(): Long {
        return prefs.getLong(KEY_LAST_CHECK, 0L)
    }
    
    /**
     * Vérifie si une vérification automatique est nécessaire
     * @return true si plus de 3 jours se sont écoulés depuis la dernière vérification
     */
    fun shouldCheckForUpdate(): Boolean {
        val lastCheck = getLastCheckTime()
        val now = System.currentTimeMillis()
        val daysSinceLastCheck = (now - lastCheck) / (1000 * 60 * 60 * 24)
        
        val shouldCheck = daysSinceLastCheck >= CHECK_INTERVAL_DAYS
        Log.d(TAG, "Jours depuis dernière vérification: $daysSinceLastCheck, Doit vérifier: $shouldCheck")
        
        return shouldCheck
    }
    
    /**
     * Enregistre la dernière version pour laquelle l'utilisateur a été notifié
     */
    fun setLastNotifiedVersion(version: String) {
        prefs.edit().putString(KEY_LAST_NOTIFIED_VERSION, version).apply()
        Log.d(TAG, "Dernière version notifiée enregistrée: $version")
    }
    
    /**
     * Récupère la dernière version pour laquelle l'utilisateur a été notifié
     */
    fun getLastNotifiedVersion(): String {
        return prefs.getString(KEY_LAST_NOTIFIED_VERSION, "") ?: ""
    }
    
    /**
     * Vérifie si l'utilisateur doit être notifié pour cette version
     * @param version La version à vérifier
     * @return true si l'utilisateur n'a pas encore été notifié pour cette version
     */
    fun shouldNotifyForVersion(version: String): Boolean {
        val lastNotified = getLastNotifiedVersion()
        val shouldNotify = lastNotified != version
        Log.d(TAG, "Dernière version notifiée: '$lastNotified', Version actuelle: '$version', Doit notifier: $shouldNotify")
        return shouldNotify
    }
    
    /**
     * Remet à zéro toutes les préférences (pour les tests)
     */
    fun reset() {
        prefs.edit().clear().apply()
        Log.d(TAG, "Préférences de mise à jour réinitialisées")
    }
    
    /**
     * Affiche l'état actuel des préférences (pour le débogage)
     */
    fun debugInfo(): String {
        val lastCheck = getLastCheckTime()
        val lastNotified = getLastNotifiedVersion()
        val shouldCheck = shouldCheckForUpdate()
        
        return """
            Préférences de mise à jour:
            - Dernière vérification: $lastCheck
            - Dernière version notifiée: '$lastNotified'
            - Doit vérifier: $shouldCheck
        """.trimIndent()
    }
}
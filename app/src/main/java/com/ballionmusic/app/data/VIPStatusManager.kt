package com.ballionmusic.app.data

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

class VIPStatusManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("vip_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_VIP_EXPIRATION = "vip_expiration"
        private const val KEY_VIP_LIFETIME = "vip_lifetime"
        private const val PROMO_BALLION = "BALLION"
        private const val PROMO_BALLION_FOREVER = "BALLIONFOREVER"
        private val THREE_MONTHS_MILLIS = TimeUnit.DAYS.toMillis(90)
    }

    fun isVIP(): Boolean {
        if (prefs.getBoolean(KEY_VIP_LIFETIME, false)) {
            return true
        }
        val expiration = prefs.getLong(KEY_VIP_EXPIRATION, 0L)
        return expiration > System.currentTimeMillis()
    }

    fun getVIPExpiration(): Long {
        return prefs.getLong(KEY_VIP_EXPIRATION, 0L)
    }

    fun applyPromoCode(code: String): Boolean {
        when (code.uppercase()) {
            PROMO_BALLION -> {
                val newExpiration = System.currentTimeMillis() + THREE_MONTHS_MILLIS
                val currentExpiration = getVIPExpiration()
                val expirationToSet = if (currentExpiration > System.currentTimeMillis()) {
                    currentExpiration + THREE_MONTHS_MILLIS
                } else {
                    newExpiration
                }
                prefs.edit().putLong(KEY_VIP_EXPIRATION, expirationToSet).apply()
                prefs.edit().putBoolean(KEY_VIP_LIFETIME, false).apply()
                return true
            }
            PROMO_BALLION_FOREVER -> {
                prefs.edit().putBoolean(KEY_VIP_LIFETIME, true).apply()
                return true
            }
            else -> return false
        }
    }

    fun purchaseVIP(months: Int = 1) {
        val currentExpiration = getVIPExpiration()
        val additionalMillis = TimeUnit.DAYS.toMillis(30L * months)
        val expirationToSet = if (currentExpiration > System.currentTimeMillis()) {
            currentExpiration + additionalMillis
        } else {
            System.currentTimeMillis() + additionalMillis
        }
        prefs.edit().putLong(KEY_VIP_EXPIRATION, expirationToSet).apply()
        prefs.edit().putBoolean(KEY_VIP_LIFETIME, false).apply()
    }

    fun renewVIP(months: Int = 1) {
        purchaseVIP(months)
    }

    fun clearVIP() {
        prefs.edit().remove(KEY_VIP_EXPIRATION).remove(KEY_VIP_LIFETIME).apply()
    }
}

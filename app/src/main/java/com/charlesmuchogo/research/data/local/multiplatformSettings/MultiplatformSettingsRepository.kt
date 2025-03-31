package com.charlesmuchogo.research.data.local.multiplatformSettings

import kotlinx.coroutines.flow.Flow

interface MultiplatformSettingsRepository {
    fun saveAppVersion(version: String)

    fun saveCurrentLatitude(latitude: Double)

    fun saveCurrentLongitude(longitude: Double)

    fun saveSelectedLatitude(latitude: Double)

    fun saveSelectedLongitude(longitude: Double)

    fun saveAccessToken(token: String)

    fun saveLoginType(loginType: String)

    fun saveUserId(id: Long)

    fun saveUserName(username: String)

    fun saveUserEmail(email: String)

    fun saveUserPhoneNumber(phone: String)

    fun getAccessToken(): Flow<String?>

    fun getAppVersion(): Flow<String?>

    fun getLoginType(): Flow<String?>

    fun getUsername(): Flow<String?>

    fun getUserEmail(): Flow<String?>

    fun getAppTheme(): Flow<Int?>

    fun getCurrentLatitude(): Flow<Double?>

    fun getCurrentLongitude(): Flow<Double?>

    fun getSelectedLatitude(): Flow<Double?>

    fun getUserId(): Flow<Long?>

    fun getSelectedLongitude(): Flow<Double?>

    fun saveAppTheme(theme: Int)

    fun clearSelectedLatitude()

    fun clearSelectedLongitude()

    fun clearSettings()
}

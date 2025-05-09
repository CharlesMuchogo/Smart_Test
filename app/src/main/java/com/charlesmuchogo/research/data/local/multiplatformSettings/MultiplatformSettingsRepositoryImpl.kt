package com.charlesmuchogo.research.data.local.multiplatformSettings

import kotlinx.coroutines.flow.Flow

class MultiplatformSettingsRepositoryImpl(private val preferenceManager: PreferenceManager) :
    MultiplatformSettingsRepository {
    override fun saveAppVersion(version: String) {
        preferenceManager.setString(key = PreferenceManager.APP_VERSION, value = version)
    }

    override fun saveCurrentLatitude(latitude: Double) {
        preferenceManager.setDouble(key = PreferenceManager.CURRENT_LATITUDE, value = latitude)
    }

    override fun saveCurrentLongitude(longitude: Double) {
        preferenceManager.setDouble(key = PreferenceManager.CURRENT_LONGITUDE, value = longitude)
    }

    override fun saveSelectedLatitude(latitude: Double) {
        preferenceManager.setDouble(key = PreferenceManager.SELECTED_LATITUDE, value = latitude)
    }

    override fun saveSelectedLongitude(longitude: Double) {
        preferenceManager.setDouble(key = PreferenceManager.SELECTED_LONGITUDE, value = longitude)
    }

    override fun saveAccessToken(token: String) {
        clearSettings()
        preferenceManager.setString(key = PreferenceManager.AUTHENTICATION_TOKEN, value = token)
    }

    override fun saveLoginType(loginType: String) {
        preferenceManager.setString(key = PreferenceManager.LOGIN_TYPE, value = loginType)
    }

    override fun saveUserId(id: Long) {
        preferenceManager.setLong(key = PreferenceManager.USER_ID, value = id)
    }

    override fun saveUserName(username: String) {
        preferenceManager.setString(key = PreferenceManager.USER_NAME, value = username)
    }

    override fun saveFirstTimeUse(firstTime: Boolean) {
        preferenceManager.setBoolean(key = PreferenceManager.FIRST_TIME, value = firstTime)
    }

    override fun saveUserEmail(email: String) {
        preferenceManager.setString(key = PreferenceManager.USER_EMAIL, value = email)
    }

    override fun saveUserPhoneNumber(phone: String) {
        preferenceManager.setString(key = PreferenceManager.USER_PHONE_NUMBER, value = phone)
    }

    override fun getAccessToken(): Flow<String?> {
        return preferenceManager.getString(PreferenceManager.AUTHENTICATION_TOKEN)
    }

    override fun getAppVersion(): Flow<String?> {
        return preferenceManager.getString(PreferenceManager.APP_VERSION)
    }

    override fun getLoginType(): Flow<String?> {
        return preferenceManager.getString(PreferenceManager.LOGIN_TYPE)
    }

    override fun getUsername(): Flow<String?> {
        return preferenceManager.getString(PreferenceManager.USER_NAME)
    }

    override fun getUserEmail(): Flow<String?> {
        return preferenceManager.getString(PreferenceManager.USER_EMAIL)
    }

    override fun getFirstTime(): Flow<Boolean?> {
        return preferenceManager.getBoolean(key = PreferenceManager.FIRST_TIME)

    }

    override fun getAppTheme(): Flow<Int?> {
        return preferenceManager.getInt(key = PreferenceManager.APP_THEME)
    }

    override fun getCurrentLatitude(): Flow<Double?> {
        return preferenceManager.getDoubleFlow(key = PreferenceManager.CURRENT_LATITUDE)
    }

    override fun getCurrentLongitude(): Flow<Double?> {
        return preferenceManager.getDoubleFlow(key = PreferenceManager.CURRENT_LONGITUDE)
    }

    override fun getSelectedLatitude(): Flow<Double?> {
        return preferenceManager.getDoubleFlow(key = PreferenceManager.SELECTED_LATITUDE)
    }

    override fun getUserId(): Flow<Long?> {
        return preferenceManager.getLong(key = PreferenceManager.USER_ID)
    }

    override fun getSelectedLongitude(): Flow<Double?> {
        return preferenceManager.getDoubleFlow(key = PreferenceManager.SELECTED_LONGITUDE)
    }

    override fun saveAppTheme(theme: Int) {
        preferenceManager.setInt(key = PreferenceManager.APP_THEME, value = theme)
    }

    override fun clearSelectedLatitude() {
        preferenceManager.clearSelectedLatitude()
    }

    override fun clearSelectedLongitude() {
        preferenceManager.clearSelectedLongitude()
    }

    override fun clearSettings() {
        preferenceManager.clearPreferences()
    }
}

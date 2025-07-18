package com.charlesmuchogo.research.data.local.multiplatformSettings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getDoubleOrNullFlow
import com.russhwolf.settings.coroutines.getIntFlow
import com.russhwolf.settings.coroutines.getIntOrNullFlow
import com.russhwolf.settings.coroutines.getLongOrNullFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow

class PreferenceManager(private val settings: Settings) {
    companion object {

        const val DATASTORE_FILE_NAME = "smart_test_shared_preferences"
        const val AUTHENTICATION_TOKEN = "auth_token_key"
        const val APP_THEME = "app_theme_key"
        const val LOGIN_TYPE = "login_type_key"
        const val FIRST_TIME = "first_time_key"

        const val APP_VERSION = "app_version"
        const val CURRENT_LATITUDE = "current_latitude_key"
        const val CURRENT_LONGITUDE = "current_longitude_key"

        const val SELECTED_LONGITUDE = "selected_latitude_key"
        const val SELECTED_LATITUDE = "selected_longitude_key"
        const val USER_NAME = "user_name_key"
        const val USER_EMAIL = "user_email_key"
        const val USER_ID = "user_id_key"
        const val USER_PHONE_NUMBER = "user_phone_number_key"
        const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741"
        const val INTERESTIAL_AD_UNIT_ID = "ca-app-pub-6323830758154106/6808883055"
    }

    private val observableSettings: ObservableSettings by lazy { settings as ObservableSettings }

    fun setString(
        key: String,
        value: String,
    ) {
        observableSettings.set(key = key, value = value)
    }

    fun getNonFlowString(key: String) =
        observableSettings.getString(
            key = key,
            defaultValue = "",
        )

    @OptIn(ExperimentalSettingsApi::class)
    fun getString(key: String) = observableSettings.getStringOrNullFlow(key = key)

    fun setInt(
        key: String,
        value: Int,
    ) {
        observableSettings.set(key = key, value = value)
    }

    @OptIn(ExperimentalSettingsApi::class)
    fun getInt(key: String) = observableSettings.getIntOrNullFlow(key = key)

    @OptIn(ExperimentalSettingsApi::class)
    fun getIntFlow(key: String) = observableSettings.getIntFlow(key = key, defaultValue = 0)

    fun setDouble(
        key: String,
        value: Double,
    ) {
        observableSettings.set(key = key, value = value)
    }

    fun getDouble(key: String) = observableSettings.getDoubleOrNull(key = key)

    @OptIn(ExperimentalSettingsApi::class)
    fun getDoubleFlow(key: String) = observableSettings.getDoubleOrNullFlow(key = key)

    fun clearPreferences() {
        observableSettings.remove(AUTHENTICATION_TOKEN)
        observableSettings.remove(USER_NAME)
        observableSettings.remove(USER_EMAIL)
        observableSettings.remove(USER_PHONE_NUMBER)
        // observableSettings.clear()
    }

    @OptIn(ExperimentalSettingsApi::class)
    fun getBoolean(key: String): Flow<Boolean> {
        return observableSettings.getBooleanFlow(
            key = key,
            defaultValue = false,
        )
    }

    fun setBoolean(
        key: String,
        value: Boolean,
    ) {
        observableSettings.set(key = key, value = value)
    }

    @OptIn(ExperimentalSettingsApi::class)
    fun getLong(key: Any): Flow<Long?> {
        return observableSettings.getLongOrNullFlow(
            key = key.toString(),
        )
    }

    fun setLong(
        key: String,
        value: Long,
    ) {
        observableSettings.set(key = key, value = value)
    }

    fun clearSelectedLatitude() {
        observableSettings.remove(SELECTED_LATITUDE)
    }

    fun clearSelectedLongitude() {
        observableSettings.remove(SELECTED_LONGITUDE)
    }
}

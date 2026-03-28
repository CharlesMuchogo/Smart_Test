package com.charlesmuchogo.research.domain.language

import androidx.annotation.StringRes
import com.charlesmuchogo.research.R

data class Language(
    @StringRes val nameRes: Int,
    @StringRes val countryRes: Int,
    val code: String,
){
    companion object
    {
        val languages = listOf(
            Language(R.string.english, R.string.united_states, "en"),
            Language(R.string.spanish, R.string.mexico, "es"),
            Language(R.string.swahili, R.string.kenya, "sw"),
            Language(R.string.french, R.string.france, "fr"),
        )
    }
}

package com.charlesmuchogo.research.domain.language

data class Language(
    val name: String,
    val country: String ,
    val code: String,
){
    companion object
    {
        val languages = listOf(
            Language("English", "United States", "en"),
            Language("Spanish", "Mexico", "es"),
            Language("Swahili", "Kenya", "sw"),
            Language("French", "France", "fr"),
        )
    }
}

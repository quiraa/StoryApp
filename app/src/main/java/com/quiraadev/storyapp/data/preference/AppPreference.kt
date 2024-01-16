package com.quiraadev.storyapp.data.preference

import com.chibatching.kotpref.KotprefModel

object AppPreference : KotprefModel() {
    var token by nullableStringPref()
    var isLoggedIn by booleanPref(false)
    var isDarkMode by booleanPref(false)
}
package com.priyanka.charmapp.authentication.data

data class UserProfile(
    var imageUrl: String = "",
    var displayName: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var aboutMe: String = "",
    var seeking: String = "",
//    val age: Int,
//    val horoscope: String = "",
)
data class PartnerSuggestion(
    val name: String,
    val age: Int,
    val horoscope: String,
    // Other suggestion attributes
)
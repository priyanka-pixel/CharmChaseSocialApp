package com.priyanka.charmapp.authentication.data

import java.util.*

data class UserProfile(
    var imageUrl: String = "",
    var displayName: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var aboutMe: String = "",
    var seeking: String = "",
    var formattedDate: String = "",
)

package com.priyanka.charmapp.authentication.data

data class UserProfile(
    var imageUrl: String = "",
    var displayName: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var seeking: String = "",
    var aboutMe: String = "",
    var gender: String = ""
)

data class PersonalInfo(
    val personalityType: PersonalityType,
    val birthdate: String,
    val interests: Interest,
    val occupation: Occupation,
    val educationLevel: EducationLevel,
    val ethnicBackground: EthnicBackground,
    val relationshipStatus: RelationshipStatus,
    val location: String
)

enum class PersonalityType(val label: String) {
    Adventurous("Adventurous"),Extrovert("Extrovert"),Funny("Funny"),
    Introvert("Introvert"), Serious("Serious"), Strict("Strict")
}

enum class Interest(val label: String) {
    Cooking("Cooking"), Family("Family"), Fashion("Fashion"),
    Films("Films"), Fitness("Fitness"), Food("Food"),Friends("Friends"),Islam("Islam"),Reading("Reading"),
    Travelling("Travelling"),TvShows("TvShows")
}
enum class Occupation(val label: String){
    Accountant("Accountant"),Analyst("Analyst"),BusinessOwner("Business Owner"),Chef("Chef"), Cleaner("Cleaner"),
    Consultant("Consultant"), CustomerService("Customer Service Representative"), Designer("Designer"),Developer("Developer"),
    Doctor("Doctor")
}
enum class EducationLevel(val label: String) {
    ALevel("A Level"), Bachelors("Bachelors"), Doctorate("Doctorate"),
    GcseLevel("Gcse Level"), Masters("Masters")
}

enum class EthnicBackground(val label: String) {
    African("African"), Arab("Arab"), Berber("Berber"), Bosniak("Bosniak"), CentralAsian("Central Asian"), Iranian("Iranian"),
    Kurdish("Kurdish"), Other("Other"), SouthAsian("South Asian"), SouthestAsian("Southest Asian"), Turkish("Turkish")
}

enum class RelationshipStatus(val label: String) {
    Single("Single"),  Married("Married"), Divorced("Divorced"),
    PreferNotToSay("Prefer Not To Say")
}

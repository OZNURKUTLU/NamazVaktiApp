package com.oznurkutlu.mynamazvakti.model

data class NamazVaktiResponse(
    val place: Place,
    val times: Map<String, List<String>>
)




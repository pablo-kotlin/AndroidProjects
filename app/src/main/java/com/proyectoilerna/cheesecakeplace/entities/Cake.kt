package com.proyectoilerna.cheesecakeplace.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Cake(var id: String = "",
                var name: String = "",
                var photoUrl: String = "",
                var postalCode: String = "",
                var city: String = "",
                var stars: Float = 0F)
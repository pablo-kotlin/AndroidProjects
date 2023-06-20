package com.proyectoilerna.cheesecakeplace.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
    data class Restaurant(var id: String = "",
                      var name: String = "",
                      var postalCode: String = "",
                      var city: String = "",
                      var urlWebsite: String = "",
                      var votes: Float = 0.0F,
                      var rating: Float = 0.0F)

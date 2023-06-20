package com.proyectoilerna.cheesecakeplace.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
    data class User(var id: String = "",
                var userName: String = "",
                var email: String = "")
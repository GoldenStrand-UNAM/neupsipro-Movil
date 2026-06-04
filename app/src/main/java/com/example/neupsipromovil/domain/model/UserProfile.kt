package com.example.neupsipromovil.domain.model

data class UserProfile(
    val fullName: String,
    val profilePhoto: String?,
    val age: Int,
    val stage: String,
    val neuroStatus: String,
    val registrationDate: String,
    val neuroEntryDate: String,
    val nextAppointmentDate: String?,
    val nextAppointmentTime: String?,
    val assignedClinic: String,
    val prosthetist: String
)
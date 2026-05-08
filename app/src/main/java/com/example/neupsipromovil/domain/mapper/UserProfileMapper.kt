package com.example.neupsipromovil.domain.mapper

import com.example.neupsipromovil.data.remote.dto.UserProfileResponse
import com.example.neupsipromovil.domain.model.UserProfile

fun UserProfileResponse.toDomain(): UserProfile {
    val info = this.data
    return UserProfile(
        fullName = info.personalInfo.fullName,
        profilePhoto = info.personalInfo.profilePhoto,
        age = info.personalInfo.age,
        stage = info.clinicalInfo.stage ?: "Sin etapa",
        neuroStatus = info.clinicalInfo.neuroStatus ?: "Desconocido",
        unitEntryDate = info.clinicalInfo.unitEntryDate ?: "--/--/--",
        neuroEntryDate = info.clinicalInfo.neuroEntryDate ?: "--/--/--",
        nextAppointmentDate = info.nextAppointment?.date,
        nextAppointmentTime = info.nextAppointment?.time,
        assignedClinic = info.assignment.assignedClinic ?: "No asignado",
        prosthetist = info.clinicalInfo.prosthetist ?: "No asignado"
    )
}
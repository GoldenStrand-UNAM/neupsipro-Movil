package com.example.neupsipromovil.domain.mapper

import com.example.neupsipromovil.data.remote.dto.UserProfileResponse
import com.example.neupsipromovil.domain.model.UserProfile

fun UserProfileResponse.toDomain(): UserProfile {
    return UserProfile(
        fullName = this.personalInfo.fullName,
        profilePhoto = this.personalInfo.profilePhoto,
        age = this.personalInfo.age,
        stage = this.clinicalInfo.stage ?: "Sin etapa",
        neuroStatus = this.clinicalInfo.neuroStatus ?: "Desconocido",
        unitEntryDate = this.clinicalInfo.unitEntryDate ?: "--/--/--",
        neuroEntryDate = this.clinicalInfo.neuroEntryDate ?: "--/--/--",
        nextAppointmentDate = this.nextAppointment?.date,
        nextAppointmentTime = this.nextAppointment?.time,
        assignedClinic = this.assignment.assignedClinic ?: "No asignado",
        prosthetist = this.clinicalInfo.prosthetist ?: "No asignado"
    )
}
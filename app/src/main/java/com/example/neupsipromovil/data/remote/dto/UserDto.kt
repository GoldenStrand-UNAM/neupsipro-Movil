package com.example.neupsipromovil.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: UserProfileData
)

data class UserProfileData(
    @SerializedName("personalInfo") val personalInfo: PersonalInfoDto,
    @SerializedName("clinicalInfo") val clinicalInfo: ClinicalInfoDto,
    @SerializedName("assignment") val assignment: AssignmentDto,
    @SerializedName("nextAppointment") val nextAppointment: AppointmentDto?
)

data class PersonalInfoDto(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("profilePhoto") val profilePhoto: String?,
    @SerializedName("birthDate") val birthDate: String?,
    @SerializedName("age") val age: Int
)

data class ClinicalInfoDto(
    @SerializedName("unitEntryDate") val unitEntryDate: String?,
    @SerializedName("neuroEntryDate") val neuroEntryDate: String?,
    @SerializedName("neuroStatus") val neuroStatus: String?,
    @SerializedName("protocol") val protocol: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("stage") val stage: String?,
    @SerializedName("prosthetist") val prosthetist: String?
)

data class AssignmentDto(
    @SerializedName("relationId") val relationId: String?,
    @SerializedName("assignedClinic") val assignedClinic: String?
)

data class AppointmentDto(
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String?
)
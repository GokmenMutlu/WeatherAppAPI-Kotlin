package com.gokmenmutlu.example2weatherapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SendCityData(
    val lat: Double,
    val lon: Double,
    val name: String
) : Parcelable

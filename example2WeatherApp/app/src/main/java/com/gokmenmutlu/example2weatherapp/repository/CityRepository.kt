package com.gokmenmutlu.example2weatherapp.repository

import com.gokmenmutlu.example2weatherapp.model.CityResponseApiModel
import com.gokmenmutlu.example2weatherapp.service.ApiServices

class CityRepository(private val api: ApiServices ) {

    suspend fun getCities(city: String, limit: Int) : List<CityResponseApiModel> {
    }

}
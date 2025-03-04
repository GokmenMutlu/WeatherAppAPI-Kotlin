package com.gokmenmutlu.example2weatherapp.viewModel

import androidx.lifecycle.ViewModel
import com.gokmenmutlu.example2weatherapp.model.CityResponseApiModel
import com.gokmenmutlu.example2weatherapp.repository.CityRepository
import com.gokmenmutlu.example2weatherapp.service.ApiServices
import com.gokmenmutlu.example2weatherapp.service.RetrofitClient

class CityViewModel(private val repository: CityRepository) : ViewModel() {
    constructor():this(CityRepository(RetrofitClient().getClient().create(ApiServices::class.java)))    //secondary constructor

    suspend fun loadCities(city: String, limit : Int) : List<CityResponseApiModel> {
        return repository.getCities(city, limit)
    }

}
package com.gokmenmutlu.example2weatherapp.viewModel

import androidx.lifecycle.ViewModel
import com.gokmenmutlu.example2weatherapp.model.CurrentResponseApiModel
import com.gokmenmutlu.example2weatherapp.model.ForecastResponseApiModel
import com.gokmenmutlu.example2weatherapp.repository.WeatherRepository
import com.gokmenmutlu.example2weatherapp.service.ApiServices
import com.gokmenmutlu.example2weatherapp.service.RetrofitClient

class WeatherViewModel(private val repository: WeatherRepository ) : ViewModel(){

    constructor():this(WeatherRepository(RetrofitClient().getClient().create(ApiServices::class.java)))
        //Secondary constructor  -> Bu sayede primary constructor a parametre verilmez ise varsayilan olarak secondary Constructor ile otomatik olarak oluşturulur.

    suspend fun loadCurrentWeather(lat:Double, lng:Double, unit:String) : CurrentResponseApiModel {
        return repository.getCurrentWeather(lat,lng,unit)
    }

    suspend fun loadForecastWeather(lat: Double,lng: Double,unit: String) : ForecastResponseApiModel {
        return repository.getForecastWeather(lat,lng,unit)
    }



}
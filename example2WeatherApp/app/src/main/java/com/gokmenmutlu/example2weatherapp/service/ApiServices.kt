package com.gokmenmutlu.example2weatherapp.service

import com.gokmenmutlu.example2weatherapp.model.CityResponseApiModel
import com.gokmenmutlu.example2weatherapp.model.CurrentResponseApiModel
import com.gokmenmutlu.example2weatherapp.model.ForecastResponseApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("data/2.5/weather")
  suspend fun getCurrentWeather(

        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("units") units: String,
        @Query("appid") ApiKey : String
    ) : CurrentResponseApiModel

    @GET("data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") ApiKey: String
    ) : ForecastResponseApiModel

    @GET("geo/1.0/direct")
    suspend fun getCities(
        @Query("q")      city : String,
        @Query("limit")  limit : Int,
        @Query("appid")  ApiKey: String
    ) : List<CityResponseApiModel>



}
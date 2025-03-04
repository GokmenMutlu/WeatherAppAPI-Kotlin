package com.gokmenmutlu.example2weatherapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gokmenmutlu.example2weatherapp.R
import com.gokmenmutlu.example2weatherapp.adapter.ForecastAdapter
import com.gokmenmutlu.example2weatherapp.databinding.FragmentMainBinding
import com.gokmenmutlu.example2weatherapp.model.SendCityData
import com.gokmenmutlu.example2weatherapp.viewModel.WeatherViewModel
import kotlinx.coroutines.launch
import java.util.Calendar


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel : WeatherViewModel by viewModels()
    private var forecastAdapter : ForecastAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forecastSwipeLayout.setOnRefreshListener {
            getData()
        }
        binding.addCityImageButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_citySelectFragment)
        }
        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getData() {

        binding.apply {

            var lat = 0.0
            var lon = 0.0
            var name = "Tekirdag"

           var cityData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
               arguments?.getParcelable<SendCityData>("sendCityData", SendCityData::class.java)
           } else {
               arguments?.getParcelable("sendCityData") // Deprecated !!!
           }
            cityData?.let { // <- CitySelectFragment'ten navigate ile geldigi zaman secilen city nin verilerini alıyor.
                lat = it.lat
                lon = it.lon
                name = it.name
            }

            if (lat == 0.0) {          // <- şehir seçilmedi ise, Açılış ekranında en son bakmış olduğu şehrin hava durumu verilerini alıyor.
                cityData = getCityLastData()    // <- SharefPreferences

                cityData?.let {
                    lat = it.lat
                    lon = it.lon
                    name = it.name
                }

            }

            //current Temp
            cityNameTxt.text = name
            progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    val data = weatherViewModel.loadCurrentWeather(lat,lon,"metric")

                    data?.let {
                        val status = it.weather?.get(0)?.main ?: "-"
                        progressBar.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                        statusTxt.text= status
                        getBackgroundWithStatus(status)
                        saveCityLastData(lat,lon,name)
                        windTxt.text = it.wind?.speed?.let { speed -> Math.round(speed).toString() + " Km"}
                        humidityTxt.text = "%${it.main?.humidity?.toString()}"
                        currentTempTxt.text= it.main?.temp?.let { temp -> Math.round(temp).toString()  + "°"}
                        maxTempTxt.text = it.main?.tempMax?.let { maxTemp -> Math.round(maxTemp).toString() + "°"}
                        minTempTxt.text = it.main?.tempMin?.let { minTemp -> Math.round(minTemp).toString()  + "°"}

                    }

                } catch (e: Exception) {
                    Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_SHORT).show()
                }
            }

            //forecast temp
            forecastAdapter = ForecastAdapter()
            recyclerForecastView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            recyclerForecastView.adapter = forecastAdapter

            lifecycleScope.launch {
                try {
                    val forecastData =   weatherViewModel.loadForecastWeather(lat,lon,"metric")
                    forecastData?.let {
                        forecastSwipeLayout.visibility = View.VISIBLE
                        val list   = it.list
                        forecastAdapter?.submitList(list)

                    }

                } catch (e:Exception) {
                    Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_SHORT).show()
                }
                finally {
                    // İşlem tamamlandı, spinner'ı kaldir
                    forecastSwipeLayout.isRefreshing = false
                }
            }

        }
    }
    
    //Background degisimi hava durumuna bagli olarak
    private fun getBackgroundWithStatus(status: String) {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
         when(status) {
            "Clouds" -> {
                if(currentHour>=18) binding.imageView.setImageResource(R.drawable.night_bg) //<- Saat kontrolü
                else {
                 binding.imageView.setImageResource(R.drawable.cloudy_bg)
                }
            }
             "Sunny","Clear" -> {
                 binding.imageView.setImageResource(R.drawable.sunny_bg)
             }
             "Snow" -> {
                binding.imageView.setImageResource(R.drawable.snow_bg)
            }
             "Rainy" -> {
                 binding.imageView.setImageResource(R.drawable.rainy_bg)
             }
             "Mist" -> {
                 binding.imageView.setImageResource(R.drawable.haze_bg)
             }
            else -> {binding.imageView.setImageResource(R.drawable.background_weather)}
        }

    }

    //SharedPreferences - SAVE
    private fun saveCityLastData(lat: Double, lon: Double, cityName: String) {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("example2WeatherAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("city_lat", lat.toFloat())  // Double'ı Float'a çeviriyoruz çünkü SharedPreferences yalnızca temel türleri destekler.
        editor.putFloat("city_lon", lon.toFloat())
        editor.putString("city_name", cityName)
        editor.apply()
    }

    //SharedPreferences - GET
    private fun getCityLastData(): SendCityData? {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("example2WeatherAppPrefs", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getFloat("city_lat", 0.0f).toDouble()  // Float'tan tekrar Double'a çevirdik
        val lon = sharedPreferences.getFloat("city_lon", 0.0f).toDouble()
        val name = sharedPreferences.getString("city_name", "Tekirdag") ?: "Tekirdag"

        // Eğer lat ve lon 0.0 ise, geçerli bir şehir kaydedilmemiş demektir.
        if (lat == 0.0 && lon == 0.0) {
            return SendCityData(40.98,27.52,"Tekirdag") // def değer olarak Tekirdag ekrana gelecek.
        }

        return SendCityData(lat, lon, name)
    }

}
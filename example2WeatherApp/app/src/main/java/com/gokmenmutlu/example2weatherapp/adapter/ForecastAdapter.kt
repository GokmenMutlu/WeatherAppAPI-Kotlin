package com.gokmenmutlu.example2weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gokmenmutlu.example2weatherapp.R
import com.gokmenmutlu.example2weatherapp.databinding.ForecastRecyclerViewholderBinding
import com.gokmenmutlu.example2weatherapp.model.ForecastResponseApiModel
import java.text.SimpleDateFormat
import java.util.Calendar

class ForecastAdapter : ListAdapter<ForecastResponseApiModel.ForecastData, ForecastAdapter.ForeCastViewHolder>(ForecastDiffCallback()) {
                     //RecyclerView Adapter ile de yapılabilir, DiffUtil kullanımında ListAdapter daha mantikli.
    class ForeCastViewHolder(val forecastBinding: ForecastRecyclerViewholderBinding) : RecyclerView.ViewHolder(forecastBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastViewHolder {
        val forecastBinding =
            ForecastRecyclerViewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForeCastViewHolder(forecastBinding)
    }

    override fun onBindViewHolder(holder: ForeCastViewHolder, position: Int) {
        val forecastData = getItem(position)
        forecastData?.let {
            updateView(holder, it)
        }
    }

    class ForecastDiffCallback : DiffUtil.ItemCallback<ForecastResponseApiModel.ForecastData>() {
        override fun areItemsTheSame(
            oldItem: ForecastResponseApiModel.ForecastData,
            newItem: ForecastResponseApiModel.ForecastData
        ): Boolean {
            return oldItem.dtTxt == newItem.dtTxt
        }

        override fun areContentsTheSame(
            oldItem: ForecastResponseApiModel.ForecastData,
            newItem: ForecastResponseApiModel.ForecastData
        ): Boolean {
            return oldItem == newItem
        }
    }

    private fun updateView(holder: ForeCastViewHolder, forecastData: ForecastResponseApiModel.ForecastData) {
        val dateParse = forecastData.dtTxt
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateParse)
        val calendar = Calendar.getInstance()
        calendar.time = date

        val dayOfWeekName = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            Calendar.SUNDAY -> "Sun"
            else -> "-"
        }
        val hour = calendar.get(Calendar.HOUR_OF_DAY).toString()

        holder.forecastBinding.apply {
            tempTxtForecast.text = forecastData.main?.temp?.let { "${Math.round(it)}°" }
            nameDayTxtForecast.text = dayOfWeekName
            hourTxtForecast.text = "$hour:00"
        }

        val context = holder.itemView.context
        val resId = ICON_MAP[forecastData.weather?.get(0)?.icon] ?: R.drawable.forecast_bg

        Glide.with(context)
            .load(resId)
            .error(R.drawable.forecast_bg)
            .into(holder.forecastBinding.forecastImage)
    }

    companion object {
        private val ICON_MAP = mapOf(
            "01d" to R.drawable.sunny,
            "01n" to R.drawable.sunny,
            "02d" to R.drawable.cloudy_sunny,
            "02n" to R.drawable.cloudy_sunny,
            "03d" to R.drawable.cloudy,
            "03n" to R.drawable.cloudy,
            "04d" to R.drawable.cloudy,
            "04n" to R.drawable.cloudy,
            "11d" to R.drawable.storm,
            "11n" to R.drawable.storm,
            "50d" to R.drawable.windy,
            "50n" to R.drawable.windy,
            "13d" to R.drawable.snowy,
            "13n" to R.drawable.snowy,
            "09d" to R.drawable.rainy,
            "09n" to R.drawable.rainy,
            "10d" to R.drawable.rainy,
            "10n" to R.drawable.rainy
        )
    }

}
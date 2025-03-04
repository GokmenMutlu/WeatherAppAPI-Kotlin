package com.gokmenmutlu.example2weatherapp.adapter

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gokmenmutlu.example2weatherapp.R
import com.gokmenmutlu.example2weatherapp.databinding.CityViewholderBinding
import com.gokmenmutlu.example2weatherapp.model.CityResponseApiModel
import com.gokmenmutlu.example2weatherapp.model.SendCityData

class CityAdapter : ListAdapter<CityResponseApiModel, CityAdapter.CityViewHolder>(CityDiffCallBack()) {
    class CityViewHolder(val cityBinding: CityViewholderBinding): ViewHolder(cityBinding.root) {

    }

    class CityDiffCallBack() :DiffUtil.ItemCallback<CityResponseApiModel>() {
        override fun areItemsTheSame(
            oldItem: CityResponseApiModel,
            newItem: CityResponseApiModel
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CityResponseApiModel,
            newItem: CityResponseApiModel
        ): Boolean {
            return oldItem == newItem
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val cityBinding = CityViewholderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        println("CityAdapter" + "Creating ViewHolder for city: ${cityBinding.cityNameViewHolderTxt.text}")
        return CityViewHolder(cityBinding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val binding = holder.cityBinding
        binding.cityNameViewHolderTxt.text = currentList[position].name


      println("CityAdapter" + "Binding ViewHolder for city: ${currentList[position].name}")

        holder.itemView.setOnClickListener {
        val bundle = Bundle()
        val cityData = SendCityData(
            currentList[position].lat ?: 0.0,
            currentList[position].lon ?: 0.0,
            currentList[position].name ?: "Tekirdag"
        )
        bundle.putParcelable("sendCityData",cityData)

        Navigation.findNavController(it).navigate(R.id.action_citySelectFragment_to_mainFragment,bundle)
    }

        }

    }

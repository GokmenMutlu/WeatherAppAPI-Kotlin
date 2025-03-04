package com.gokmenmutlu.example2weatherapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gokmenmutlu.example2weatherapp.adapter.CityAdapter
import com.gokmenmutlu.example2weatherapp.databinding.FragmentCitySelectBinding
import com.gokmenmutlu.example2weatherapp.viewModel.CityViewModel
import kotlinx.coroutines.launch


class CitySelectFragment : Fragment() {

    private var _binding: FragmentCitySelectBinding? = null
    private val binding get() = _binding!!
    private val cityViewModel: CityViewModel by viewModels()
    private var cityAdapter: CityAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCitySelectBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityAdapter = CityAdapter()
        binding.cityRecyclerView.adapter = cityAdapter
        binding.cityRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        getCities()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCities() {
        binding.apply {
            citySearchEditTxt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Placeholder
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    // Placeholder
                }

                override fun afterTextChanged(s: Editable?) {
                    progressBarSearchCity.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        try {
                            val cityData = cityViewModel.loadCities(s.toString(), 3)

                            cityData?.let {
                               cityAdapter?.submitList(it)
                            }
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), e.localizedMessage ?: "Error", Toast.LENGTH_SHORT).show()
                            println(e.toString())
                        }
                        progressBarSearchCity.visibility = View.GONE
                    }
                }
            })
        }
    }
}


package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.ItemAdapter
import com.dicoding.asclepius.databinding.FragmentHealthNewsListDialogBinding
import com.dicoding.asclepius.network.ApiConfig
import com.dicoding.asclepius.network.HealthNewsResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val ARG_ITEM_COUNT = "item_count"

class HealthNewsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentHealthNewsListDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHealthNewsListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        fetchArticles()
    }

    private fun fetchArticles() {
        val apiService = ApiConfig.apiService
        apiService.getCancerArticles(
            query = "cancer",
            category = "health",
            language = "en",
            apiKey = "5dcead5ce16c41cf8b7fbca015b28a2c"
        ).enqueue(object : Callback<HealthNewsResponse> {
            override fun onResponse(
                call: Call<HealthNewsResponse>,
                response: Response<HealthNewsResponse>,
            ) {
                if (response.isSuccessful) {
                    response.body()?.articles?.let { articles ->
                        binding.list.layoutManager = LinearLayoutManager(requireActivity())
                        val adapter = ItemAdapter(articles)
                        binding.list.adapter = adapter
                    } ?: run {
                        showToast("No articles found in response.")
                    }
                } else {
                    showToast("Request failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<HealthNewsResponse>, t: Throwable) {
                showToast("Request failed: ${t.message}")
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}
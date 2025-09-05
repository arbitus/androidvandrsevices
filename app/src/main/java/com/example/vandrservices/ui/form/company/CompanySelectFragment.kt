package com.example.vandrservices.ui.form.company

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vandrservices.R
import com.example.vandrservices.data.ApiService
import com.example.vandrservices.data.CompanyUI
import com.example.vandrservices.data.RetrofitControles
import com.example.vandrservices.databinding.FragmentCompanySelectBinding
import com.example.vandrservices.domain.model.User
import com.example.vandrservices.ui.form.isInternetAvailable
import com.example.vandrservices.ui.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class CompanySelectFragment : Fragment() {

    private var _binding: FragmentCompanySelectBinding? = null
    private val binding get() = _binding!!

    private lateinit var retrofit: Retrofit
    private lateinit var adapter: CompanyAdapter

    private val userViewModel: LoginViewModel by activityViewModels()
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanySelectBinding.inflate(inflater, container, false)
        retrofit = RetrofitControles.getRetrofit()
        apiService = retrofit.create(ApiService::class.java)
        initUI()
        return binding.root
    }

    private fun initUI() {
        setupRecyclerView()
        loadCompanies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToFormLot(id: Int) {
        val bundle = Bundle().apply {
            putInt("id", id)
        }
        findNavController().navigate(R.id.lotCreationFragment, bundle)
    }
    private fun cleanUrl(url: String?): String {
        return url
            ?.replaceFirst("^https://https://".toRegex(), "https://")
            ?.replaceFirst("(qd94ebxicome/)\\1".toRegex(), "$1")
            ?: ""
    }
    private fun setupRecyclerView() {
        adapter = CompanyAdapter { id -> navigateToFormLot(id) }
        binding.companyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.companyRecyclerView.adapter = adapter
    }
    private fun loadCompanies(){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            if (isInternetAvailable(requireContext())) {
                // Obtén el usuario guardado y su token
                val user: User? = userViewModel.usersFlow.firstOrNull()?.firstOrNull()
                val token = user?.token

                if (!token.isNullOrEmpty()) {
                    val responseCompanys = apiService.getCompanys("Bearer $token")
                    withContext(Dispatchers.Main) {
                        if (responseCompanys.isSuccessful) {
                            val companyList = responseCompanys.body() ?: emptyList()
                            val uiList = companyList.map { company ->
                                CompanyUI(company.id, company.name, cleanUrl(company.url))
                            }
                            adapter.updateList(uiList)
                        }
                    }
                }
            } else {
                Snackbar.make(binding.root, "Company load in local", Snackbar.LENGTH_LONG).show()
                withContext(Dispatchers.Main) {
                    adapter.loadOfflineCompanies()
                }
            }
        }
    }
}
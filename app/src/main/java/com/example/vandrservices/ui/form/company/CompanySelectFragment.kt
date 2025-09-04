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

    private val apiService by lazy { retrofit.create(ApiService::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanySelectBinding.inflate(inflater, container, false)
        retrofit = RetrofitControles.getRetrofit()
        initUI()
        return binding.root
    }

    private fun initUI() {
        adapter = CompanyAdapter { NavigateToFormLot(it) }
        binding.companyRecicleView.layoutManager = LinearLayoutManager(requireContext())
        binding.companyRecicleView.adapter = adapter

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
                                val cleanUrl = company.url
                                    ?.replaceFirst("^https://https://".toRegex(), "https://")
                                    ?.replaceFirst("(qd94ebxicome/)\\1".toRegex(), "$1")
                                    ?: ""
                                CompanyUI(
                                    id = company.id,
                                    name = company.name,
                                    imageUrl = cleanUrl
                                )
                            }
                            adapter.updateList(uiList)
                        } else {
                            Log.e("vandrservices", "Error en getCompanys: ${responseCompanys.errorBody()?.string()}")
                            loadOfflineCompanies()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("vandrservices", "No se encontró token guardado en usuario.")
                        loadOfflineCompanies()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    loadOfflineCompanies()
                }
            }
        }
    }

    private fun loadOfflineCompanies() {
        val offlineList = listOf(
            CompanyUI(133, "San Jorge", imageRes = R.drawable.san_jorge),
            CompanyUI(166, "Pure Nature Produce", imageRes = R.drawable.pure_nature),
            CompanyUI(199, "Dayka Hackett LLC", imageRes = R.drawable.daykahackettfrutura_color),
            CompanyUI(232, "Frutura Texas", imageRes = R.drawable.frutura_logo),
            CompanyUI(4, "Ground Fresh International", imageRes = R.drawable.ground_fresh),
            CompanyUI(3, "Coliman", imageRes = R.drawable.coliman),
            CompanyUI(1, "Level Berries", imageRes = R.drawable.level_berries),
            CompanyUI(2, "BST", imageRes = R.drawable.bst),
            CompanyUI(265, "Produce Nation", imageRes = R.drawable.produce_notion),
            CompanyUI(70, "Chanitos", imageRes = R.drawable.chanitos),
            CompanyUI(34, "Ganfer Fresh", imageRes = R.drawable.ganfer),
            CompanyUI(100, "Farmacopia Farms", imageRes = R.drawable.farmacopia),
        )
        adapter.updateList(offlineList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun NavigateToFormLot(id: Int) {
        val bundle = Bundle().apply {
            putInt("id", id)
        }
        findNavController().navigate(R.id.lotCreationFragment, bundle)
    }
}
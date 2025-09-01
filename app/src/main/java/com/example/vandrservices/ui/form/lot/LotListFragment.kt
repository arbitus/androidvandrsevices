package com.example.vandrservices.ui.form.lot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vandrservices.databinding.FragmentLotListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LotListFragment : Fragment() {

    private var _binding: FragmentLotListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LotListViewModel by viewModels()
    private lateinit var adapter: LotAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLotListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = LotAdapter(emptyList())
        binding.rvLots.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLots.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.lots.collectLatest { lots ->
                adapter.submitList(lots)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

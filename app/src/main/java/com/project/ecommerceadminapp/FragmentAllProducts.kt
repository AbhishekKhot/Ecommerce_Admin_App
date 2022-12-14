package com.project.ecommerceadminapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.project.ecommerceadminapp.databinding.FragmentAllProductsBinding


class FragmentAllProducts : Fragment() {
    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAllProductsBinding.inflate(layoutInflater)

        binding.fbAddProduct.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentAllProducts_to_fragmentAddNewProducts)
        }
        return binding.root
    }

}
package com.project.ecommerceadminapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.project.ecommerceadminapp.databinding.FragmentAddNewProductsBinding

class FragmentAddNewProducts : Fragment() {
    private var _binding: FragmentAddNewProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNewProductsBinding.inflate(layoutInflater)
        return binding.root
    }
}
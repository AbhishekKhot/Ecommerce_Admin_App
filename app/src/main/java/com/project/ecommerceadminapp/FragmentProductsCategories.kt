package com.project.ecommerceadminapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.ecommerceadminapp.databinding.FragmentProductsCategoriesBinding

class FragmentProductsCategories : Fragment() {
    private var _binding:FragmentProductsCategoriesBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentProductsCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }
}
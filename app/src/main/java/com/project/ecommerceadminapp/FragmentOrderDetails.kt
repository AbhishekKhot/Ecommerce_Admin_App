package com.project.ecommerceadminapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.ecommerceadminapp.databinding.FragmentOrderDetailsBinding

class FragmentOrderDetails : Fragment() {
    private var _binding:FragmentOrderDetailsBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentOrderDetailsBinding.inflate(layoutInflater)
        return binding.root
    }
}
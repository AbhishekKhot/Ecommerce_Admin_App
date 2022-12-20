package com.project.ecommerceadminapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.ecommerceadminapp.R
import com.project.ecommerceadminapp.adapters.ProductAdapter
import com.project.ecommerceadminapp.databinding.FragmentAllProductsBinding
import com.project.ecommerceadminapp.models.ProductDetails


class FragmentAllProducts : Fragment() {
    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding!!
    private val firebase = Firebase.firestore
    private lateinit var productDetailsAdapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllProductsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productDetailsAdapter = ProductAdapter(requireActivity())
        binding.fbAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAllProducts_to_fragmentAddNewProducts)
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val list = ArrayList<ProductDetails>()
        firebase.collection("Products").get()
            .addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(ProductDetails::class.java)
                    list.add(data!!)
                }
                binding.recyclerViewProducts.adapter = productDetailsAdapter
                list.reverse()
                productDetailsAdapter.differ.submitList(list)
            }
    }

}
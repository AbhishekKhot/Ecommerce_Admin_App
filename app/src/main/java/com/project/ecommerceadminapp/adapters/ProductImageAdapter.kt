package com.project.ecommerceadminapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.ecommerceadminapp.databinding.ProductImageItemBinding

class ProductImageAdapter(val list: ArrayList<Uri>) :
    RecyclerView.Adapter<ProductImageAdapter.ProductImageViewHolder>() {
    inner class ProductImageViewHolder(val binding: ProductImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val binding =
            ProductImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        holder.binding.ivProducts.setImageURI(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
package com.project.ecommerceadminapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.ecommerceadminapp.R
import com.project.ecommerceadminapp.databinding.ProductItemBinding
import com.project.ecommerceadminapp.models.ProductDetails

class ProductAdapter(val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    val diffCallback = object : DiffUtil.ItemCallback<ProductDetails>() {
        override fun areItemsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
            return oldItem.product_id == newItem.product_id
        }

        override fun areContentsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        Glide.with(holder.itemView).load(product.product_cover_image)
            .placeholder(R.drawable.ic_image_search).into(holder.binding.ivProduct)
        holder.binding.apply {
            tvProductName.text = product.product_name
            tvProductDescription.text = product.product_description
            tvProductSellingPrice.text = product.product_selling_price
            tvProductPrice.text = product.product_mrp
            tvProductCategory.text = product.product_category
            btnDelete.setOnClickListener {
                val alert = AlertDialog.Builder(context)
                alert.setTitle("DELETE")
                    .setMessage("DO YOU WANT TO DELETE THIS PRODUCT ?")
                    .setNegativeButton("NO", null)
                    .setPositiveButton("YES") { dialog, which ->
                        val fireStore = Firebase.firestore
                        fireStore.collection("Products")
                            .whereEqualTo("product_id", product.product_id).get()
                            .addOnCompleteListener {
                                for (snapshot in it.result) {
                                    fireStore.collection("Products")
                                        .document(snapshot.id).delete()
                                }
                            }
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, differ.currentList.size)
                    }
                alert.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
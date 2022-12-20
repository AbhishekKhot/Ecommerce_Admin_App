package com.project.ecommerceadminapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.project.ecommerceadminapp.R
import com.project.ecommerceadminapp.adapters.ProductImageAdapter
import com.project.ecommerceadminapp.databinding.FragmentAddNewProductsBinding
import com.project.ecommerceadminapp.models.Category
import com.project.ecommerceadminapp.models.ProductDetails
import java.util.*
import kotlin.collections.ArrayList

class FragmentAddNewProducts : Fragment() {
    private var _binding: FragmentAddNewProductsBinding? = null
    private val binding get() = _binding!!
    private val firebase= Firebase.firestore
    private lateinit var list:ArrayList<Uri>
    private lateinit var listItems:ArrayList<String>
    private lateinit var adapter: ProductImageAdapter
    private var coverImage:Uri?=null
    private lateinit var dialog: Dialog
    private var coverImageURL:String?=""
    private lateinit var categoryList:ArrayList<String>

    private var launchGalleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                coverImage = it.data!!.data
                binding.ivCoverProduct.setImageURI(coverImage)
                binding.ivCoverProduct.visibility=View.VISIBLE
            }
        }

    @SuppressLint("NotifyDataSetChanged")
    private var launchProductImagesActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val imageURL = it.data!!.data
                list.add(imageURL!!)
                adapter.notifyDataSetChanged()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewProductsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list=ArrayList()
        listItems=ArrayList()

        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)

        setUpCategorySpinner()

        binding.tvCoverIv.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }

        binding.tvProductsIv.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchProductImagesActivity.launch(intent)
        }

        adapter= ProductImageAdapter(list)
        binding.recyclerViewIvProducts.adapter=adapter

        binding.btnAddProduct.setOnClickListener {
            checkFieldValues()
        }

    }

    private fun checkFieldValues() {
        if(binding.etProductName.text.toString().isEmpty()){
            binding.etProductName.requestFocus()
            binding.etProductName.error="Empty"
        } else if(binding.etProductDescription.text.toString().isEmpty()){
            binding.etProductDescription.requestFocus()
            binding.etProductDescription.error="Empty"
        } else if(binding.etProductPrice.text.toString().isEmpty()){
            binding.etProductPrice.requestFocus()
            binding.etProductPrice.error="Empty"
        } else if(binding.etProductSP.text.toString().isEmpty()){
            binding.etProductSP.requestFocus()
            binding.etProductSP.error="Empty"
        } else if(coverImage==null){
            Toast.makeText(requireContext(),"Please select product cover image",Toast.LENGTH_SHORT).show()
        } else if(list.size<1){
            Toast.makeText(requireContext(),"Please select product images",Toast.LENGTH_SHORT).show()
        } else{
            uploadImageToStorage()
        }
    }

    private fun uploadImageToStorage() {
        dialog.show()
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val storageReference = FirebaseStorage.getInstance().reference.child("Products/$fileName")
        storageReference.putFile(coverImage!!).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { image ->
                coverImageURL=image.toString()
                uploadProductsImagesToStorage()
            }
        }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireActivity(),
                    it.message.toString() + "Failed",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private var i=0
    private fun uploadProductsImagesToStorage() {
        dialog.show()
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val storageReference = FirebaseStorage.getInstance().reference.child("Products/$fileName")
        storageReference.putFile(list[i]).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { image ->
                listItems.add(image.toString())
                if(list.size==listItems.size){
                    uploadDataToFireStore()
                }
                else{
                    i += 1
                    uploadProductsImagesToStorage()
                }
            }
        }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireActivity(),
                    it.message.toString() + "Failed",
                    Toast.LENGTH_SHORT).show()
            }
    }


    private fun uploadDataToFireStore() {
        val db=firebase.collection("Products")
        val key = db.document().id
        val data = ProductDetails(
            binding.etProductName.text.toString(),
            binding.etProductDescription.text.toString(),
            coverImageURL,
            categoryList[binding.spCategories.selectedItemPosition],
            key,
            binding.etProductPrice.text.toString(),
            binding.etProductSP.text.toString(),
            listItems
        )

        db.document(key).set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"new Product added successfully",Toast.LENGTH_SHORT).show()
                binding.etProductName.text=null
                binding.etProductDescription.text=null
                binding.etProductPrice.text=null
                binding.etProductSP.text=null
                binding.ivCoverProduct.visibility=View.INVISIBLE
                binding.recyclerViewIvProducts.visibility=View.INVISIBLE
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),it.message+"Failed",Toast.LENGTH_SHORT).show()
            }
    }


    private fun setUpCategorySpinner() {
        categoryList=ArrayList()
        firebase.collection("Categories").get()
            .addOnSuccessListener {
                categoryList.clear()
                for(doc in it.documents){
                    val data=doc.toObject(Category::class.java)
                    categoryList.add(data!!.category_name!!)
                }
                categoryList.add(0,"Choose Category")

                val spinnerAdapter = ArrayAdapter(requireContext(),
                    R.layout.spinner_item,categoryList)
                binding.spCategories.adapter=spinnerAdapter
            }
    }


}
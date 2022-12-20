package com.project.ecommerceadminapp.models

data class ProductDetails (
    var product_name:String?="",
    var product_description:String?="",
    var product_cover_image:String?="",
    var product_category:String?="",
    var product_id:String?="",
    var product_mrp:String?="",
    var product_selling_price:String?="",
    var product_images:ArrayList<String> = ArrayList()
)
package com.example.maslahty.data.models

import com.google.gson.annotations.SerializedName

data class TransferRequestDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("vehicle_id")
    val vehicleId: String,
    @SerializedName("seller_id")
    val sellerId: String,
    @SerializedName("buyer_id")
    val buyerId: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("status")
    val status: String,
    @SerializedName("seller_name")
    val sellerName: String,
    @SerializedName("buyer_name")
    val buyerName: String,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("updated_at")
    val updatedAt: Long,
    @SerializedName("notes")
    val notes: String = "",
    @SerializedName("price_warning")
    val priceWarning: PriceWarningDto? = null
)

data class PriceWarningDto(
    @SerializedName("market_price")
    val marketPrice: Double,
    @SerializedName("difference")
    val difference: Double,
    @SerializedName("percentage")
    val percentage: Double,
    @SerializedName("message")
    val message: String
)


package com.example.maslahty.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transfer_requests")
data class TransferRequestEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "vehicle_id")
    val vehicleId: String,
    @ColumnInfo(name = "seller_id")
    val sellerId: String,
    @ColumnInfo(name = "buyer_id")
    val buyerId: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "seller_name")
    val sellerName: String,
    @ColumnInfo(name = "buyer_name")
    val buyerName: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "notes")
    val notes: String,
    @ColumnInfo(name = "market_price")
    val marketPrice: Double?,
    @ColumnInfo(name = "price_difference")
    val priceDifference: Double?,
    @ColumnInfo(name = "price_percentage")
    val pricePercentage: Double?,
    @ColumnInfo(name = "price_warning_message")
    val priceWarningMessage: String?
)


package com.example.maslahty.data.mappers

import com.example.maslahty.data.local.entities.TransferRequestEntity
import com.example.maslahty.data.models.PriceWarningDto
import com.example.maslahty.data.models.TransferRequestDto
import com.example.maslahty.domain.entities.PriceWarning
import com.example.maslahty.domain.entities.TransferRequest
import com.example.maslahty.domain.entities.TransferStatus
import java.util.Date

fun TransferRequestDto.toEntity(): TransferRequestEntity {
    return TransferRequestEntity(
        id = id,
        vehicleId = vehicleId,
        sellerId = sellerId,
        buyerId = buyerId,
        price = price,
        status = status,
        sellerName = sellerName,
        buyerName = buyerName,
        createdAt = createdAt,
        updatedAt = updatedAt,
        notes = notes,
        marketPrice = priceWarning?.marketPrice,
        priceDifference = priceWarning?.difference,
        pricePercentage = priceWarning?.percentage,
        priceWarningMessage = priceWarning?.message
    )
}

fun TransferRequestEntity.toDomain(): TransferRequest {
    val priceWarning = if (marketPrice != null && priceDifference != null && pricePercentage != null && priceWarningMessage != null) {
        PriceWarning(
            marketPrice = marketPrice,
            difference = priceDifference,
            percentage = pricePercentage,
            message = priceWarningMessage
        )
    } else {
        null
    }

    return TransferRequest(
        id = id,
        vehicleId = vehicleId,
        sellerId = sellerId,
        buyerId = buyerId,
        price = price,
        status = TransferStatus.valueOf(status),
        sellerName = sellerName,
        buyerName = buyerName,
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt),
        notes = notes,
        priceWarning = priceWarning
    )
}

fun TransferRequest.toEntity(): TransferRequestEntity {
    return TransferRequestEntity(
        id = id,
        vehicleId = vehicleId,
        sellerId = sellerId,
        buyerId = buyerId,
        price = price,
        status = status.name,
        sellerName = sellerName,
        buyerName = buyerName,
        createdAt = createdAt.time,
        updatedAt = updatedAt.time,
        notes = notes,
        marketPrice = priceWarning?.marketPrice,
        priceDifference = priceWarning?.difference,
        pricePercentage = priceWarning?.percentage,
        priceWarningMessage = priceWarning?.message
    )
}

fun TransferRequestDto.toDomain(): TransferRequest {
    val priceWarning = priceWarning?.let {
        PriceWarning(
            marketPrice = it.marketPrice,
            difference = it.difference,
            percentage = it.percentage,
            message = it.message
        )
    }

    return TransferRequest(
        id = id,
        vehicleId = vehicleId,
        sellerId = sellerId,
        buyerId = buyerId,
        price = price,
        status = TransferStatus.valueOf(status),
        sellerName = sellerName,
        buyerName = buyerName,
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt),
        notes = notes,
        priceWarning = priceWarning
    )
}


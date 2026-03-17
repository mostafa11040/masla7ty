package com.example.maslahty.domain.utils

import com.example.maslahty.domain.entities.PriceWarning

object PriceValidationUtil {
    fun validatePrice(askedPrice: Double, marketPrice: Double): PriceWarning? {
        val difference = askedPrice - marketPrice
        val percentage = (difference / marketPrice) * 100

        return when {
            percentage > 30 -> {
                PriceWarning(
                    marketPrice = marketPrice,
                    difference = difference,
                    percentage = percentage,
                    message = "تحذير: السعر أعلى من السوق بـ ${"%.1f".format(percentage)}%"
                )
            }
            percentage < -30 -> {
                PriceWarning(
                    marketPrice = marketPrice,
                    difference = difference,
                    percentage = percentage,
                    message = "تحذير: السعر أقل من السوق بـ ${"%.1f".format(Math.abs(percentage))}%"
                )
            }
            else -> null
        }
    }

    fun getMarketPriceEstimate(
        model: String,
        year: Int,
        kilometers: Int,
        condition: String
    ): Double {
        // This is a simplified estimation. In production, this would call a backend API
        // For now, returning a fixed calculation
        val basePrice = 30000.0 // Base price in Saudi Riyals
        val ageMultiplier = (2024 - year) * 0.05 // 5% depreciation per year
        val kmMultiplier = (kilometers / 100000) * 0.02 // 2% per 100k km
        val conditionMultiplier = when (condition) {
            "EXCELLENT" -> 1.2
            "VERY_GOOD" -> 1.0
            "GOOD" -> 0.85
            "FAIR" -> 0.7
            else -> 0.5
        }

        return basePrice * (1 - ageMultiplier - kmMultiplier) * conditionMultiplier
    }
}


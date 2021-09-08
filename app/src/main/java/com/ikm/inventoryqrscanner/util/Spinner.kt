package com.ikm.inventoryqrscanner.util

fun spinnerCondition(string: String?) : Int {
    return when {
        string.isNullOrEmpty() -> {
            0
        }
        string == "Baik" -> {
            1
        }
        string == "Cukup" -> {
            2
        }
        else -> {
            3
        }
    }
}

fun spinnerType(string: String?) : Int {
    return when {
        string.isNullOrEmpty() -> {
            0
        }
        string == "Berbahaya" -> {
            1
        }
        else -> {
            2
        }
    }
}
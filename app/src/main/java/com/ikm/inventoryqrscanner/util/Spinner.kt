package com.ikm.inventoryqrscanner.util

fun spinnerCondition(string: String) : Int {
    if (string == "Baik"){
        return 0
    }else if (string == "Cukup"){
        return 1
    }else{
        return 2
    }
}

fun spinnerType(string: String) : Int {
    if (string == "Berbahaya"){
        return 0
    }else
        return 1
}
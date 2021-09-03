package com.ikm.inventoryqrscanner.util

fun spinnerCondition(string: String?) : Int {
    if (string.isNullOrEmpty()){
        return 0
    }else if (string == "Baik"){
        return 1
    }else if (string == "Cukup"){
        return 2
    }else{
        return 3
    }
}

fun spinnerType(string: String?) : Int {
    if (string.isNullOrEmpty()){
        return 0
    }else if (string == "Berbahaya"){
        return 1
    }else{
        return 2
    }
}
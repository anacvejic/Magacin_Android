package com.example.magacin.enteties

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TempProduct (
    val idProduct: Int,
    val productName: String,
    val productCode: String,
    val sumOfAmount: Int = 0
): Parcelable
package com.example.magacin.enteties

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "product_table")
@Parcelize
data class Product(
    @ColumnInfo(name = "productName")
    val productName: String,
    @ColumnInfo(name = "productCode")
    val productCode: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idProduct")
    val idProduct: Int = 0
) : Parcelable

package com.example.magacin.enteties

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.magacin.enteties.Product
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(
    tableName = "typeOfProduct_table",
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("idProduct"),
        childColumns = arrayOf("productId"),
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class TypeOfProduct(
    @ColumnInfo(name = "productId")
    val productId: Int,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name = "amount")
    val priceOfTypeProduct: Int,
    @ColumnInfo(name = "invoiceNumber")
    val invoiceNumber: Int,
    @ColumnInfo(name = "created")
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idProductType")
    val idProductType: Int = 0
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}
package com.example.magacin.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.magacin.enteties.Product
import com.example.magacin.enteties.TempProduct
import com.example.magacin.enteties.TypeOfProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface MagacinDao {

    @Query("SELECT * FROM product_table WHERE productName LIKE '%' || :searchQuery || '%' ORDER BY productName ASC")
    fun getProducts(searchQuery: String): Flow<List<Product>>

    @Query("SELECT * FROM typeOfProduct_table WHERE productId = :productId ORDER BY created DESC")
    fun getTypeOfProducts(productId: Int): Flow<List<TypeOfProduct>>

    @Query(
        "SELECT idProduct, productName, productCode," +
                "COALESCE(SUM(amount), 0) AS sumOfAmount FROM product_table LEFT JOIN typeOfProduct_table " +
                "on idProduct == productId " +
                "WHERE productName LIKE '%' || :searchQuery || '%'" +
                "GROUP BY idProduct, productName, productCode ORDER BY productName DESC"
    )
    fun getProductWithAmount(searchQuery: String): Flow<List<TempProduct>>

    @Query("SELECT MAX(invoiceNumber) FROM typeOfProduct_table")
    fun getMaxInvoiceNUmber(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductType(typeOfProduct: TypeOfProduct)

    @Update
    suspend fun updateProduct(product: Product)

    @Update
    suspend fun updateTypeOfProduct(typeOfProduct: TypeOfProduct)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Delete
    suspend fun deleteTypeOfProduct(typeOfProduct: TypeOfProduct)
}
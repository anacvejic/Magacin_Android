package com.example.magacin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.magacin.enteties.Product
import com.example.magacin.enteties.TypeOfProduct

@Database(entities = [Product::class, TypeOfProduct::class], version = 1, exportSchema = false)
abstract class MagacinDatabase: RoomDatabase() {

    abstract fun magacinDao(): MagacinDao
}
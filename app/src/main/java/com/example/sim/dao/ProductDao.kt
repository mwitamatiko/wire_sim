package com.example.sim.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sim.entity.Product

@Dao
interface ProductDao {
    @Insert
    fun insertProduct(product: Product)

    @Query("select * from products where productName = :name")
    fun findProduct(name: String): List<Product>

    @Query("delete from products where productName = :name")
    fun deleteProduct(name: String)

    @Query("select * from products")
    fun findAllProduct(): LiveData<List<Product>>
    
}
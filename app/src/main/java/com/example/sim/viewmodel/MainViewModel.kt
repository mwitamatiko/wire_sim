package com.example.sim.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sim.db.ProductRoomDatabase
import com.example.sim.entity.Product
import com.example.sim.repo.ProductRepository
import com.example.sim.utils.CipherUtils

class MainViewModel(application: Application): ViewModel() {
    val allProducts: LiveData<List<Product>>
    private val repository: ProductRepository
    val searchResults :MutableLiveData<List<Product>>

    init {
        val productDb = ProductRoomDatabase.getInstance(application)
        val productDao = productDb.productDao()
        repository = ProductRepository(productDao)

        allProducts = repository.allProducts
        searchResults=repository.searchResults
    }

    fun insertProduct(product: Product){
        val data = repository.insertProduct(product)
        Log.d("data-->"," productName: "+product.productName+" quantity: "+product.quantity)

    }

    fun findProduct(name: String){
        repository.findProduct(name)
    }

    fun deleteProduct(name: String){
        repository.deleteProduct(name)
    }

}
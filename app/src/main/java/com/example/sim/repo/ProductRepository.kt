package com.example.sim.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sim.dao.ProductDao
import com.example.sim.entity.Product
import com.example.sim.utils.CipherUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProductRepository(private val productDao: ProductDao) {

    //store results of an asynchronous search
    val searchResults = MutableLiveData<List<Product>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val allProducts: LiveData<List<Product>> = productDao.findAllProduct()

    fun insertProduct(newProduct: Product){
        coroutineScope.launch(Dispatchers.IO) {
            productDao.insertProduct(newProduct)

        }
    }
    fun deleteProduct(name: String){
        coroutineScope.launch(Dispatchers.IO) {
            productDao.deleteProduct(name)
        }
    }

    fun findProduct(name: String){
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }

    private fun asyncFind(name: String): Deferred<List<Product>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async productDao.findProduct(name)
        }

}
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
    val cipherUtils = CipherUtils()


    fun insertProduct(newProduct: Product){
        coroutineScope.launch(Dispatchers.IO) {
//            Log.d("data from viewmodel"," productName: "+newProduct.productName+" quantity: "+newProduct.quantity)
//            val key = cipherUtils.generateKey()
//            Log.d("key-->","generated key: "+key.toString())
//            val productName = cipherUtils.encrypt(newProduct.productName.toByteArray(), key)
//            Log.d("encrypted","encrypted productName "+productName)
//            val quantity = cipherUtils.encrypt(newProduct.quantity.toString().toByteArray(),key)
//            Log.d("encrypted","encrypted quantity "+quantity)
//            val encryptedProduct = Product(productName.toString(),quantity.toString().toInt())
//            Log.d("encrypted","encrypted encryptedProduct "+encryptedProduct)
//            productDao.insertProduct(encryptedProduct)
            productDao.insertProduct(newProduct)
//            Log.d("data"," productName: "+newProduct.productName +" quantity: "+ newProduct.quantity)

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
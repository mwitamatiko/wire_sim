package com.example.sim.encryption

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.InvalidKeyException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import kotlin.random.Random

object SqliteEncryptorImpl: SqliteEncryptor  {
    override fun decrypt(context: Context, file: File, alias: String) {
        println("to be implemented")
    }

    override fun encrypt(context: Context, file: File, alias: String) {
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        Log.e("alias",""+alias)
        val key =  retrieveKeyFromKeystore(alias) ?: generateKey(alias)
        Log.e("generatedkey",""+key)
        cipher.init(Cipher.ENCRYPT_MODE,key)

        val encryptedFileName = "${genRandomName()}_${file.name}"
        val encryptedFileDbFile = File(context.filesDir,encryptedFileName)

        Log.e("encryptedFileDbFile",""+encryptedFileDbFile)

        FileInputStream(file).use {input->
            FileOutputStream(encryptedFileDbFile).use { output->
                val outputCipherStream = CipherOutputStream(output,cipher)
                input.copyTo(outputCipherStream)
                outputCipherStream.close()
            }

        }
    }

    override fun generateKey(alias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,"AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()

        keyGenerator.init(keyGenParameterSpec)

        val secretKey = keyGenerator.generateKey()

        //store key in android keystore
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        keyStore.setEntry(alias,KeyStore.SecretKeyEntry(secretKey),null)

        Log.e("keystore"," "+keyStore)

        return secretKey
    }

    fun retrieveKeyFromKeystore(alias: String): SecretKey?{
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore.getKey(alias,null) as? SecretKey
    }

    fun genRandomName(): String{
        val alphaString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..10)
            .map { Random.nextInt(0,alphaString.length) }
            .map { alphaString::get }
            .joinToString("")
    }
}
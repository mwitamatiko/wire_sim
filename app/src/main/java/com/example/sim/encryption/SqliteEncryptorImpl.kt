package com.example.sim.encryption

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import kotlin.random.Random

object SqliteEncryptorImpl: SqliteEncryptor  {

    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    private val cipher = Cipher.getInstance(TRANSFORMATION)

    private val IV_LENGTH=12

    override fun decrypt(context: Context, file: File, alias: String) {

        println("starting decryption")
        Log.e("alias",""+alias)
        val key = retrieveKeyFromKeystore(alias)
        Log.e("retrievedKey",""+key)

        Log.e("filename",""+file)

        val iv = ByteArray(IV_LENGTH)

        FileInputStream(file).use {input ->
            input.read(iv,0,iv.size)

            val gcmParameterSpec = GCMParameterSpec(128,iv)

            cipher.init(Cipher.DECRYPT_MODE,key,gcmParameterSpec)

            val decryptedFileName = file.name
            Log.i("decryptedFileName: ",""+decryptedFileName)
            val dbDir = context.getDatabasePath("databases").parentFile
            Log.i("dbdirpath: ",""+dbDir)

            val decryptedDbFile = File(dbDir,decryptedFileName)
            Log.i("decryptedDbFile",""+decryptedDbFile)

            FileOutputStream(decryptedDbFile).use { output ->
                FileInputStream(file).use { input ->
                    val inputCipherStream = CipherInputStream(input, cipher)
                    inputCipherStream.copyTo(output)
                    inputCipherStream.close()
                }
            }
            input.close()
        }
        println("end decrypting")

    }

    //works
    override fun encrypt(context: Context, file: File, alias: String) {
        Log.e("alias",""+alias)
        val key =  retrieveKeyFromKeystore(alias)
        Log.e("generatedkey",""+key)
        cipher.init(Cipher.ENCRYPT_MODE,key)
        val iv = cipher.iv

        val encryptedFileName = file.name
        Log.i("encryptedFileName: ",""+encryptedFileName)
        val dbDir = context.getDatabasePath("databases").parentFile

        Log.i("dbdirpath: ",""+dbDir)
        val encryptedDbFile = File(dbDir,encryptedFileName)

        Log.i("encryptedFileDbFile",""+encryptedDbFile)

        FileOutputStream(encryptedDbFile).use { output->
            output.write(iv.size)
            output.write(iv)
            FileInputStream(file).use { input ->
                val outputCipherStream = CipherOutputStream(output,cipher)
                input.copyTo(outputCipherStream)
                outputCipherStream.close()
            }
        }
    }

    // looks okay
    override fun generateKey(alias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(false)
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(keyGenParameterSpec)

        val secretKey = keyGenerator.generateKey()
        Log.e("secretKeygenerated"," "+secretKey)

        return secretKey
    }


//     works okay
    fun retrieveKeyFromKeystore(alias: String): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val existingKey = keyStore.getEntry(alias,null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: generateKey(alias)
    }

}
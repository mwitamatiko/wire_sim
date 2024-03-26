package com.example.sim.utils

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CipherUtils {

    //generate random key
    fun generateKey(keySize: Int=128): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(keySize)
        return keyGenerator.generateKey()

    }

    //encryption
    fun encrypt(input: ByteArray, secretKey: SecretKey): ByteArray{
        val aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(ByteArray(16))
        aesCipher.init(Cipher.ENCRYPT_MODE,secretKey,ivParameterSpec)
        return  aesCipher.doFinal(input)
    }

    //decryption
    fun decrypt(input: ByteArray,secretKey: SecretKey):ByteArray{
        val aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(ByteArray(16))
        aesCipher.init(Cipher.DECRYPT_MODE,secretKey,ivParameterSpec)
        return  aesCipher.doFinal(input)
    }
}
package com.example.sim.encryption

import android.content.Context
import java.io.File
import javax.crypto.SecretKey

interface SqliteEncryptor {
    fun encrypt(context: Context, file: File,alias: String)
    fun decrypt(context: Context,file: File,alias: String)
    fun generateKey(alias: String): SecretKey
}
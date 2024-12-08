package com.ricardosfp.zapping.infrastructure.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

// todo interface this
object ObjectToByte {
    fun serialize(obj: Any): ByteArray? {
        try {
            val out = ByteArrayOutputStream()
            val os = ObjectOutputStream(out)
            os.writeObject(obj)
            return out.toByteArray()
        }
        catch (ex: Exception) {
        }
        return null
    }

    fun deserialize(data: ByteArray): Any? {
        try {
            val input = ByteArrayInputStream(data)
            val inputStream = ObjectInputStream(input)
            return inputStream.readObject()
        }
        catch (ex: Exception) {
        }
        return null
    }
}
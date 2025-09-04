package com.example.vandrservices.data.local.dataStore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.vandrservices.DamageListProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object DamageSerializer : Serializer<DamageListProto> {
    override val defaultValue: DamageListProto = DamageListProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): DamageListProto {
        try {
            return DamageListProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: DamageListProto, output: OutputStream) = t.writeTo(output)
}
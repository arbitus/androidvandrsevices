package com.example.vandrservices.data.local.dataStore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.vandrservices.PaletListProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream


object PaletSerializer : Serializer<PaletListProto> {
    override val defaultValue: PaletListProto = PaletListProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): PaletListProto {
        try {
            return PaletListProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: PaletListProto, output: OutputStream) = t.writeTo(output)
}
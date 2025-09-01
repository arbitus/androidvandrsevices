package com.example.vandrservices.data.local.dataStore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.vandrservices.LotListProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object LotSerializer : Serializer<LotListProto> {
    override val defaultValue: LotListProto = LotListProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LotListProto {
        try {
            return LotListProto.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: LotListProto, output: OutputStream) = t.writeTo(output)
}
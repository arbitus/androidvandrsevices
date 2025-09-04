package com.example.vandrservices.data.local.dataStore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.vandrservices.UserListProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserSerializer : Serializer<UserListProto> {
    override val defaultValue: UserListProto = UserListProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserListProto {
        try {
            return UserListProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserListProto, output: OutputStream) = t.writeTo(output)
}
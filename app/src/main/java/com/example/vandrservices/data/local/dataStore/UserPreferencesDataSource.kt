package com.example.vandrservices.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.vandrservices.UserEntityProto
import com.example.vandrservices.UserListProto
import com.example.vandrservices.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore: DataStore<UserListProto> by dataStore(
    fileName = "user.pb",
    serializer = UserSerializer
)

class UserPreferencesDataSource(private val context: Context) {

    fun getUsers(): Flow<List<UserEntity>> =
        context.userDataStore.data.map { proto ->
            proto.usersList.map {
                UserEntity(
                    it.userId, it.name, it.password, it.token
                )
            }
        }


    suspend fun addUser(user: UserEntity) {
        context.userDataStore.updateData { proto ->
            proto.toBuilder()
                .addUsers(
                    UserEntityProto.newBuilder()
                        .setUserId(user.userId)
                        .setName(user.name)
                        .setPassword(user.password)
                        .setToken(user.token)
                        .build()
                ).build()
        }
    }

    /** NUEVO: Actualiza el token de un usuario existente (por userId) */
    suspend fun updateUserToken(userId: String, newToken: String) {
        context.userDataStore.updateData { proto ->
            val updatedUsers = proto.usersList.map { userProto ->
                if (userProto.userId == userId) {
                    userProto.toBuilder()
                        .setToken(newToken)
                        .build()
                } else {
                    userProto
                }
            }
            proto.toBuilder()
                .clearUsers()
                .addAllUsers(updatedUsers)
                .build()
        }
    }
}
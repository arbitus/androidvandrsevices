package com.example.vandrservices.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.vandrservices.DamageEntityProto
import com.example.vandrservices.DamageListProto
import com.example.vandrservices.PaletEntityProto
import com.example.vandrservices.data.local.model.DamageEntity
import com.example.vandrservices.data.local.model.PaletEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.damageDataStore: DataStore<DamageListProto> by dataStore(
    fileName = "damages.pb",
    serializer = DamageSerializer
)

class DamagePreferencesDataSource(private val context: Context) {

    fun getDamages(): Flow<List<DamageEntity>> =
        context.damageDataStore.data.map { proto ->
            proto.damagesList.map {
                DamageEntity(
                    it.localId, it.palet, it.name, it.type, it.value
                )
            }
        }


    suspend fun addDamage(damage: DamageEntity) {
        context.damageDataStore.updateData { proto ->
            proto.toBuilder()
                .addDamages(
                    DamageEntityProto.newBuilder()
                        .setLocalId(damage.localId)
                        .setPalet(damage.palet)
                        .setName(damage.name)
                        .setType(damage.type)
                        .setValue(damage.value ?: 0.0)
                        .build()
                ).build()
        }
    }

    suspend fun clearDamages() {
        context.damageDataStore.updateData {
            it.toBuilder().clearDamages().build()
        }
    }

    suspend fun deleteDamage(localId: String) {
        context.damageDataStore.updateData { proto ->
            val newList = proto.damagesList.filter { it.localId != localId }
            proto.toBuilder().clearDamages().addAllDamages(newList).build()
        }
    }

    fun getDamagesByPaletId(paletId: Int): Flow<List<DamageEntity>> =
        context.damageDataStore.data.map { proto ->
            proto.damagesList
                .filter { it.palet == paletId }
                .map {
                    DamageEntity(
                        it.localId, it.palet, it.name, it.type, it.value
                    )
                }
        }

}
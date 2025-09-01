package com.example.vandrservices.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.vandrservices.LotEntityProto
import com.example.vandrservices.LotListProto
import com.example.vandrservices.data.local.dataStore.LotSerializer
import com.example.vandrservices.data.local.model.LotEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.lotDataStore: DataStore<LotListProto> by dataStore(
    fileName = "lots.pb",
    serializer = LotSerializer
)

class LotPreferencesDataSource(private val context: Context) {

    fun getLots(): Flow<List<LotEntity>> =
        context.lotDataStore.data.map { proto ->
            proto.lotsList.map {
                LotEntity(
                    it.localId,it.company ,it.lotNumber, it.arrivalPort, it.insPlace, it.insDate,
                    it.exporter, it.invoice, it.arvWeek, it.origin, it.auditor,
                    it.cases, it.grower, it.label, it.variety
                )
            }
        }

    suspend fun addLot(lot: LotEntity) {
        context.lotDataStore.updateData { proto ->
            proto.toBuilder()
                .addLots(
                    LotEntityProto.newBuilder()
                        .setLocalId(lot.localId)
                        .setCompany(lot.company)
                        .setLotNumber(lot.lotNumber)
                        .setArrivalPort(lot.arrivalPort)
                        .setInsPlace(lot.insPlace)
                        .setInsDate(lot.insDate)
                        .setExporter(lot.exporter)
                        .setInvoice(lot.invoice)
                        .setArvWeek(lot.arvWeek)
                        .setOrigin(lot.origin)
                        .setAuditor(lot.auditor)
                        .setCases(lot.cases)
                        .setGrower(lot.grower)
                        .setLabel(lot.label)
                        .setVariety(lot.variety)
                        .build()
                ).build()
        }
    }

    suspend fun clearLots() {
        context.lotDataStore.updateData {
            it.toBuilder().clearLots().build()
        }
    }
}
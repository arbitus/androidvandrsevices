package com.example.vandrservices.data

import com.example.vandrservices.domain.model.DamageToJson
import com.example.vandrservices.domain.model.Lot
import com.example.vandrservices.domain.model.LotToJson
import com.example.vandrservices.domain.model.LotsImgResponse
import com.example.vandrservices.domain.model.Palet
import com.example.vandrservices.domain.model.PaletToJson
import com.example.vandrservices.domain.model.PaletsImgResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("api/companys/")
    suspend fun getCompanys(
        @Header("Authorization") authHeader: String
    ): Response<List<CompanyResponse>>

    @FormUrlEncoded
    @POST("api/token/")
    suspend fun PostToken(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<TokenResponse>

    @POST("api/lots/")
    suspend fun createLot(
        @Header("Authorization") authHeader: String,
        @Body lot: LotToJson
    ): Response<LotToJson>

    @POST("api/palets/")
    suspend fun createPalet(
        @Header("Authorization") authHeader: String,
        @Body palet: PaletToJson
    ): Response<PaletToJson>

    @POST("api/damages/")
    suspend fun createDamage(
        @Header("Authorization") authHeader: String,
        @Body damage: DamageToJson
    ): Response<DamageToJson>

    @Multipart
    @POST("lots-img/multiple-bulk/")
    suspend fun uploadLotImages(
        @Header("Authorization") authHeader: String,
        @Part lotNumber: MultipartBody.Part,
        @Part tipos: List<MultipartBody.Part>,
        @Part imagenes: List<MultipartBody.Part>
    ): Response<List<LotsImgResponse>>

    @Multipart
    @POST("palets-img/multiple-bulk/")
    suspend fun uploadPaletImages(
        @Header("Authorization") authHeader: String,
        @Part lotNumber: MultipartBody.Part,
        @Part paletNumber: MultipartBody.Part,
        @Part tipos: List<MultipartBody.Part>,
        @Part imagenes: List<MultipartBody.Part>
    ): Response<List<PaletsImgResponse>>
}
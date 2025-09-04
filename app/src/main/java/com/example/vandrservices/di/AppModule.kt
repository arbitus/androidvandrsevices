package com.example.vandrservices.di

import android.content.Context
import com.example.vandrservices.data.local.dataStore.DamagePreferencesDataSource
import com.example.vandrservices.data.local.dataStore.LotPreferencesDataSource
import com.example.vandrservices.data.local.dataStore.PaletPreferencesDataSource
import com.example.vandrservices.data.repository.DamageRepositoryImpl
import com.example.vandrservices.data.repository.LotRepositoryImpl
import com.example.vandrservices.data.repository.PaletRepositoryImpl
import com.example.vandrservices.domain.repository.DamageRepository
import com.example.vandrservices.domain.repository.LotRepository
import com.example.vandrservices.domain.repository.PaletRepository
import com.example.vandrservices.domain.usecase.AddDamageUseCase
import com.example.vandrservices.domain.usecase.AddLotUseCase
import com.example.vandrservices.domain.usecase.AddPaletUseCase
import com.example.vandrservices.domain.usecase.ClearDamagesUseCase
import com.example.vandrservices.domain.usecase.ClearLotsUseCase
import com.example.vandrservices.domain.usecase.ClearPaletsUseCase
import com.example.vandrservices.domain.usecase.GetDamagesUseCase
import com.example.vandrservices.domain.usecase.GetLotsUseCase
import com.example.vandrservices.domain.usecase.GetPaletsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLotPreferencesDataSource(
        @ApplicationContext context: Context
    ): LotPreferencesDataSource {
        return LotPreferencesDataSource(context)
    }

    @Provides @Singleton
    fun provideLotRepository(dataSource: LotPreferencesDataSource): LotRepository =
        LotRepositoryImpl(dataSource)

    @Provides fun provideAddLotUseCase(repository: LotRepository) = AddLotUseCase(repository)
    @Provides fun provideGetLotsUseCase(repository: LotRepository) = GetLotsUseCase(repository)
    @Provides fun provideClearLotsUseCase(repository: LotRepository) = ClearLotsUseCase(repository)

    @Provides
    @Singleton
    fun providePaletPreferencesDataSource(
        @ApplicationContext context: Context
    ): PaletPreferencesDataSource {
        return PaletPreferencesDataSource(context)
    }

    @Provides @Singleton
    fun providePaletRepository(dataSource: PaletPreferencesDataSource): PaletRepository =
        PaletRepositoryImpl(dataSource)

    @Provides fun provideAddPaletUseCase(repository: PaletRepository) = AddPaletUseCase(repository)
    @Provides fun provideGetPaletsUseCase(repository: PaletRepository) = GetPaletsUseCase(repository)
    @Provides fun provideClearPaletsUseCase(repository: PaletRepository) = ClearPaletsUseCase(repository)

    @Provides
    @Singleton
    fun provideDamagePreferencesDataSource(
        @ApplicationContext context: Context
    ): DamagePreferencesDataSource {
        return DamagePreferencesDataSource(context)
    }

    @Provides @Singleton
    fun provideDamageRepository(dataSource: DamagePreferencesDataSource): DamageRepository =
        DamageRepositoryImpl(dataSource)

    @Provides fun provideAddDamageUseCase(repository: DamageRepository) = AddDamageUseCase(repository)
    @Provides fun provideGetDamagesUseCase(repository: DamageRepository) = GetDamagesUseCase(repository)
    @Provides fun provideClearDamagesUseCase(repository: DamageRepository) = ClearDamagesUseCase(repository)
}
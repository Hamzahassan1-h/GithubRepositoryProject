/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// InjectionModule.kt
package com.example.android.codelabs.paging.di

import android.content.Context
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.data.GithubRepository
import com.example.android.codelabs.paging.data.db.RepoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Injection {
    @Provides
    @Singleton
    fun provideGithubService(): GithubService {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(GithubService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepoDatabase(@ApplicationContext context: Context): RepoDatabase {
        return RepoDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideGithubRepository(
        service: GithubService,
        database: RepoDatabase
    ): GithubRepository {
        return GithubRepository(service, database)
    }

//    @Provides
//    @Singleton
//    fun provideViewModelFactory(
//        repository: GithubRepository
//    ): ViewModelProvider.Factory {
//        return ViewModelFactory(repository)
//    }
}

//
//    /**
//     * Creates an instance of [GithubRepository] based on the [GithubService] and a
//     * [GithubLocalCache]
//     */
//    private fun provideGithubRepository(context: Context): GithubRepository {
//        return GithubRepository(GithubService.create(), RepoDatabase.getInstance(context))
//    }
//
//    /**
//     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
//     * [ViewModel] objects.
//     */
//    fun provideViewModelFactory(context: Context, owner: SavedStateRegistryOwner): ViewModelProvider.Factory {
//        return ViewModelFactory(owner, provideGithubRepository(context))
//    }

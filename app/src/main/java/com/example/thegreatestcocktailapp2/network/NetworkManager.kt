package fr.isen.coignet.thegreatestcocktailapp2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/" // URL de base de l'API

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
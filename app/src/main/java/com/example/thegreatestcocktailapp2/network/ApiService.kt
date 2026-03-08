package fr.isen.coignet.thegreatestcocktailapp2.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService { // Interface Retrofit qui liste tous les appels possibles à l'API
    @GET("random.php")   // Récupère un cocktail aléatoire
    fun getRandomCocktail(): Call<Drinks>

    @GET("filter.php")  // Récupère la liste des cocktails d'une catégorie donnée
    fun getDrinksByCategory(@Query("c") category: String): Call<Drinks>

    @GET("lookup.php") // Récupère les détails d'un cocktail à partir de son ID
    fun getDrinkById(@Query("i") id: String): Call<Drinks>

}
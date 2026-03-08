package fr.isen.coignet.thegreatestcocktailapp2.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// Objet global qui contient la liste renvoyée par l'API
class Drinks(
    val drinks: List<DrinkModel>
) : Serializable

// Détails d'un cocktail spécifique,représente un cocktail avec toutes ses données
class DrinkModel(
    @SerializedName("idDrink") val id: String = "",
    @SerializedName("strDrink") val name: String = "",
    @SerializedName("strCategory") val category: String = "",
    @SerializedName("strDrinkThumb") val imageURL: String = "",
    @SerializedName("strInstructions") val instructions: String = "",
    @SerializedName("strGlass") val strGlass: String? = null,

    // Les ingrédients et mesures sont null si le cocktail en a moins de 6
    //pour les ingrédients et les mesures
    @SerializedName("strIngredient1") val strIngredient1: String? = null,
    @SerializedName("strMeasure1") val strMeasure1: String? = null,
    @SerializedName("strIngredient2") val strIngredient2: String? = null,
    @SerializedName("strMeasure2") val strMeasure2: String? = null,
    @SerializedName("strIngredient3") val strIngredient3: String? = null,
    @SerializedName("strMeasure3") val strMeasure3: String? = null,
    @SerializedName("strIngredient4") val strIngredient4: String? = null,
    @SerializedName("strMeasure4") val strMeasure4: String? = null,
    @SerializedName("strIngredient5") val strIngredient5: String? = null,
    @SerializedName("strMeasure5") val strMeasure5: String? = null,
    @SerializedName("strIngredient6") val strIngredient6: String? = null,
    @SerializedName("strMeasure6") val strMeasure6: String? = null,
) : Serializable
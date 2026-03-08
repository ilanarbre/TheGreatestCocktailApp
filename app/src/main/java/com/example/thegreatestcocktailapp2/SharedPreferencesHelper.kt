package fr.isen.coignet.thegreatestcocktailapp2

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.isen.coignet.thegreatestcocktailapp2.network.DrinkModel


// Classe utilitaire pour sauvegarder et récupérer les cocktails favoris
// Utilise SharedPreferences pour conserver les favoris même après fermeture de l'app
class SharedPreferencesHelper(context: Context) {
    private val key = "favDrinks" // Clé utilisée pour stocker et retrouver les favoris dans les SharedPreferences
    private val sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE) // Instance des SharedPreferences liée à cette clé
    private val gson = Gson() // Gson permet de convertir la liste de DrinkModel en JSON et inversement

    fun saveFavoriteList(list: ArrayList<DrinkModel>) { // Sauvegarde la liste complète des favoris en JSON dans les SharedPreferences
        val json = gson.toJson(list)
        sharedPreferences.edit { putString(key, json) }
    }


    // Récupère la liste des favoris depuis les SharedPreferences
    // Retourne une liste vide si aucun favori n'a encore été sauvegardé
    fun getFavoriteList(): ArrayList<DrinkModel> {
        val json = sharedPreferences.getString(key, null)
        val type = object : TypeToken<ArrayList<DrinkModel>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }
}
package fr.isen.coignet.thegreatestcocktailapp2

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.coignet.thegreatestcocktailapp2.network.DrinkModel
import fr.isen.coignet.thegreatestcocktailapp2.network.Drinks
import fr.isen.coignet.thegreatestcocktailapp2.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DrinksScreen(modifier: Modifier, category: String) { // Écran affichant la liste des cocktails d'une catégorie donnée
    // Liste des cocktails récupérés depuis l'API
    val drinks = remember { mutableStateOf<List<DrinkModel>>(listOf()) }


    // Appel API au chargement de l'écran pour récupérer les cocktails de la catégorie
    // Les espaces dans le nom de catégorie sont remplacés par des "_" pour l'URL
    LaunchedEffect(Unit) {
        val call = NetworkManager.apiService.getDrinksByCategory(category.replace(" ", "_"))
        call.enqueue(object : Callback<Drinks> {
            override fun onResponse(p0: Call<Drinks>, p1: Response<Drinks>) { // On met à jour la liste avec les cocktails reçus
                drinks.value = p1.body()?.drinks ?: listOf()
            }

            override fun onFailure(p0: Call<Drinks>, p1: Throwable) {
                Log.e("error", p1.message.toString())
            }
        })
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF450346)) // le fond violet
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Titre de la catégorie
        item {
            Text(category, color = Color.White, fontSize = 50.sp)
        }

        // Liste des cocktails
        items(drinks.value) { drink -> // Un bouton par cocktail, clic → ouvre le détail du cocktail
            val context = LocalContext.current
            Button(
                onClick = { // On envoie l'ID du cocktail à DetailCocktailActivity
                    val intent = Intent(context, DetailCocktailActivity::class.java)
                    intent.putExtra("drinkID", drink.id)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                // TON DESIGN : Boutons noirs transparents
                shape = RoundedCornerShape(size = 25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.3f),
                    contentColor = Color.White
                )
            ) {
                // TON DESIGN : Texte blanc 30sp
                Text(drink.name, fontSize = 30.sp)
            }
        }
    }
}
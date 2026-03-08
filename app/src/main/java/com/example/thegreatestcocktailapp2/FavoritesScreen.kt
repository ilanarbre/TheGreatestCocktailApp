package fr.isen.coignet.thegreatestcocktailapp2

import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.coignet.thegreatestcocktailapp2.network.DrinkModel

@Composable
fun FavoritesScreen(modifier: Modifier) { // Écran affichant la liste des cocktails mis en favoris par l'utilisateur
    val sharedPreferences = SharedPreferencesHelper(LocalContext.current) // Accès aux SharedPreferences pour lire les favoris sauvegardés
    // val favList = sharedPreferences.getFavoriteList()
    var favList by remember { mutableStateOf<List<DrinkModel>>(emptyList()) } // Liste des favoris, mise à jour dès que l'écran s'affiche

    LaunchedEffect(Unit) { // Charge la liste des favoris depuis les SharedPreferences à chaque affichage
        favList = sharedPreferences.getFavoriteList()
    }
    LazyColumn( // Affichage de la liste en scroll vertical
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF450346)) //Fond violet
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(favList) { drink -> // Un bouton par favori, clic → ouvre le détail du cocktail
            val context = LocalContext.current
            Button(
                onClick = {
                    // On envoie l'ID du cocktail à DetailCocktailActivity
                    val intent = Intent(context, DetailCocktailActivity::class.java)
                    intent.putExtra("drinkID", drink.id)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.3f),
                    contentColor = Color.White
                )
            ) {
                Text(drink.name, fontSize = 30.sp)
            }
        }
    }
}
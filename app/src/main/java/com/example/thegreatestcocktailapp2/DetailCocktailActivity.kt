package fr.isen.coignet.thegreatestcocktailapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateOf
import fr.isen.coignet.thegreatestcocktailapp2.network.DrinkModel


// Activity qui affiche le détail d'un cocktail spécifique
// Lancée depuis DrinksScreen ou FavoritesScreen avec l'ID du cocktail en paramètre
class DetailCocktailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent { // On récupère l'ID du cocktail envoyé par l'écran précédent
            val drinkId = intent.getStringExtra("drinkID")

            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                val snackBarHostState = remember { SnackbarHostState() } // Pour les messages de confirmation (ajout/suppression favori)
                val currentDrink = remember { mutableStateOf<DrinkModel?>(null) } // Le cocktail chargé, partagé avec la TopAppBar pour le bouton favori
                Scaffold(
                    topBar = {  // La flèche retour ferme cette Activity et revient à l'écran précédent
                        TopAppBar(snackBarHostState,currentDrink.value, onBack = { finish() })
                    }
                ) { innerPadding ->
                    // On donne l'ID au design pour qu'il charge le bon cocktail, onDrinkLoaded remonte le cocktail à la TopAppBar dès qu'il est chargé
                    DetailCocktailScreen(
                        modifier = Modifier.padding(innerPadding),
                        drinkID = drinkId,
                        onDrinkLoaded = { currentDrink.value = it }
                    )
                }
            }
        }
    }
}
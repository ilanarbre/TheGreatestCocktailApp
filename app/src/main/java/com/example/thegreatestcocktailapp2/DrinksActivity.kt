/* package fr.isen.coignet.thegreatestcocktailapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class DrinksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Logique Prof : on récupère la catégorie envoyée par CategoriesScreen
        val category = intent.getStringExtra("category").toString()

        setContent {
            // Remplacement du thème par Surface pour ton projet sans ui.theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                val snackBarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(snackBarHostState, onBack = { finish() })
                { innerPadding ->
                    DrinksScreen(
                        modifier = Modifier.padding(innerPadding),
                        category = category
                    )
                }
            }
        }
    }
}
    */

package fr.isen.coignet.thegreatestcocktailapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color



// Activity qui affiche la liste des cocktails d'une catégorie
// Lancée depuis CategoriesScreen avec le nom de la catégorie en paramètre
class DrinksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // On récupère le nom de la catégorie envoyé par CategoriesScreen
        val category = intent.getStringExtra("category").toString()

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                val snackBarHostState = remember { SnackbarHostState() } // Pour les messages de confirmation
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        // La flèche retour ferme cette Activity et revient aux catégories
                        TopAppBar(snackBarHostState, onBack = { finish() })
                    }
                ) { innerPadding ->
                    // On passe la catégorie à DrinksScreen pour qu'il charge les bons cocktails
                    DrinksScreen(
                        modifier = Modifier.padding(innerPadding),
                        category = category
                    )
                }
            }
        }
    }
}
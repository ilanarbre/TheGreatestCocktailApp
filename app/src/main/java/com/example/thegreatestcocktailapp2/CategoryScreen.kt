
package fr.isen.coignet.thegreatestcocktailapp2

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// Écran affichant la liste des catégories de cocktails disponibles
@Composable
fun CategoriesScreen(modifier: Modifier) {  // Liste des catégories en statique orrespondant aux catégories de l'API
    val categories = listOf(
        "Beer",
        "Cocktail",
        "Cocoa",
        "Coffee / Tea",
        "Homemade Liqueur",
        "Ordinary Drink",
        "Punch / Party Drink",
        "Shake",
        "Shot",
        "Soft drink",
        "Other / Unknown"
    )

    // Affichage de la liste en scroll vertical
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF450346)) // Fond violet
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Un bouton par catégorie, clic → ouvre la liste des cocktails de cette catégorie
        items(categories) { category ->
            val context = LocalContext.current
            Button(
                onClick = {
                    // On envoie le nom de la catégorie à DrinksActivity
                    val intent = Intent(context, DrinksActivity::class.java)
                    intent.putExtra("category", category)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.3f), // Boutons derrière le texte
                    contentColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified,
                    disabledContentColor = Color.Unspecified
                )
            ) {
                Text(text = category, color = Color.White, fontSize = 30.sp)
            }
        }
    }
}
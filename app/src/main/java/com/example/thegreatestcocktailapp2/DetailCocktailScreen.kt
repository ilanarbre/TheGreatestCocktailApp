package fr.isen.coignet.thegreatestcocktailapp2

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.coignet.thegreatestcocktailapp2.network.DrinkModel
import fr.isen.coignet.thegreatestcocktailapp2.network.Drinks
import fr.isen.coignet.thegreatestcocktailapp2.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Ingredient(val ingredient: String, val measure: String) // On associe chaque ingrédient à sa quantité

@Composable
fun DetailCocktailScreen(modifier: Modifier, drinkID: String? = null, onDrinkLoaded: ((DrinkModel) -> Unit)? = null) {
    // Le cocktail sera mis à jour une fois la réponse de l'API reçue
    val drink = remember { mutableStateOf(DrinkModel()) }

    // Si on vient de la liste, on charge le cocktail demandé, sinon on en prend un au hasard
    LaunchedEffect(drinkID) {
        val call = if (!drinkID.isNullOrEmpty()) {
            NetworkManager.apiService.getDrinkById(drinkID)
        } else {
            NetworkManager.apiService.getRandomCocktail()
        }

        call.enqueue(object : Callback<Drinks> {
            override fun onResponse(p0: Call<Drinks>, p1: Response<Drinks>) {
                // On met à jour l'affichage et on prévient la TopAppBar que le cocktail est prêt
                drink.value = p1.body()?.drinks?.firstOrNull() ?: DrinkModel()
                onDrinkLoaded?.invoke(drink.value)
            }
            override fun onFailure(p0: Call<Drinks>, p1: Throwable) {
                Log.e("API_ERROR", p1.message.toString())
            }
        })
    }
// Construction de la liste des ingrédients. On filtre les ingrédients vides car tous les cocktails n'en ont pas le même nombre
    val ingredientsList = listOfNotNull(
        drink.value.strIngredient1?.let { Ingredient(it, drink.value.strMeasure1 ?: "") },
        drink.value.strIngredient2?.let { Ingredient(it, drink.value.strMeasure2 ?: "") },
        drink.value.strIngredient3?.let { Ingredient(it, drink.value.strMeasure3 ?: "") },
        drink.value.strIngredient4?.let { Ingredient(it, drink.value.strMeasure4 ?: "") },
        drink.value.strIngredient5?.let { Ingredient(it, drink.value.strMeasure5 ?: "") },
        drink.value.strIngredient6?.let { Ingredient(it, drink.value.strMeasure6 ?: "") }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image du cocktail chargée depuis la base de donnée api
        AsyncImage(
            model = drink.value.imageURL,
            contentDescription = drink.value.name,
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        //titre du cocktail en majuscules
        Text(
            text = drink.value.name.uppercase(),
            fontSize = 32.sp,
            fontFamily = FontFamily.Serif,
            color = Color(0xFF450346),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        // card violet : catégorie du cocktail
        Surface(
            color = Color(0xFFC97EFD),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = drink.value.category,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }
        //card rose : type de verre recommandé
        Surface(
            color = Color(0xFFFF00A8),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = " ${drink.value.strGlass ?: ""}",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }
// Card orange : liste des ingrédients avec leurs mesures
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800))
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = "INGREDIENTS",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(10.dp))
                ingredientsList.forEach { ingredient ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = ingredient.ingredient, color = Color.Black)
                        Text(text = ingredient.measure, color = Color.Black)
                    }
                }
            }
        }
// Card jaune : instructions de préparation
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD54F))
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = "RECIPE",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = drink.value.instructions,
                    color = Color.Black,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
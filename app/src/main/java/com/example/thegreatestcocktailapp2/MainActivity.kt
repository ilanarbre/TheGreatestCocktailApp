package fr.isen.coignet.thegreatestcocktailapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.coignet.thegreatestcocktailapp2.network.DrinkModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.ArrowBack

enum class NavigationItem( // Les 3 onglets de navigation avec leur icône et leur route
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    Home("Home", Icons.Default.Home, "home"), // Cocktail aléatoire
    List("Categories", Icons.Default.Menu, "list"), // Liste des catégories
    Fav("Favoris", Icons.Default.Favorite, "fav") //favoris
}

class MainActivity : ComponentActivity() { // Point d'entrée de l'application
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                val snackBarHostState = remember { SnackbarHostState() } // Permet d'afficher des messages temporaires en bas de l'écran
                val navController = rememberNavController() // Contrôleur de navigation entre les 3 onglets
                val startNavigationItem = NavigationItem.Home // L'onglet affiché au démarrage de l'app
                val currentNavigationItem = remember { mutableStateOf(startNavigationItem) } // On suit l'onglet sélectionné pour mettre à jour la barre du bas
                val currentDrink = remember { mutableStateOf<DrinkModel?>(null) } // Le cocktail actuellement affiché, partagé avec la TopAppBar pour le bouton favori
                val currentRoute = remember { mutableStateOf(startNavigationItem.route) } // La route actuelle, pour savoir si on affiche la flèche retour ou pas

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { // La TopAppBar reçoit le cocktail actuel et la logique de retour selon la route
                        TopAppBar(snackBarHostState, currentDrink.value, onBack = if (currentRoute.value != "home") {{ navController.popBackStack() }} else null // Flèche retour sur tous les écrans sauf Home
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(snackBarHostState)
                    },
                    bottomBar = { // Barre de navigation en bas avec les 3 onglets
                        NavigationBar {
                            NavigationItem.entries.forEach { navigationItem ->
                                NavigationBarItem( // L'onglet est surligné si c'est celui actuellement affiché
                                    selected = currentNavigationItem.value == navigationItem,
                                    onClick = {
                                        navController.navigate(navigationItem.route)
                                        currentNavigationItem.value = navigationItem
                                    },
                                    label = {
                                        Text(navigationItem.title)
                                    },
                                    icon = {
                                        Icon(navigationItem.icon, contentDescription = "")
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startNavigationItem.route
                    ) {
                        composable(NavigationItem.Home.route) { // Onglet Home : charge un cocktail aléatoire et remonte le résultat à la TopAppBar
                            DetailCocktailScreen(
                                Modifier.padding(innerPadding),
                                onDrinkLoaded = { currentDrink.value = it }
                                )
                            currentRoute.value = "home"
                        }
                        composable(NavigationItem.List.route) { // Onglet Categories : affiche la liste des catégories de cocktails
                            CategoriesScreen(modifier = Modifier.padding(innerPadding))
                            currentRoute.value = "list"
                        }
                        composable(NavigationItem.Fav.route) { // Onglet Favoris : affiche les cocktails mis en favoris par le user
                            FavoritesScreen(modifier = Modifier.padding(innerPadding))
                            currentRoute.value = "fav"
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TopAppBar commune à tous les écrans
// Reçoit le cocktail affiché pour gérer le bouton favori
// Reçoit onBack pour afficher ou non la flèche retour
fun TopAppBar(snackbarHostState: SnackbarHostState, drink: DrinkModel? = null,  onBack: (() -> Unit)? = null) {
    val context = LocalContext.current
    val sharedPrefs = SharedPreferencesHelper(context) // Accès aux SharedPreferences pour lire/écrire les favoris
    val snackbarScope = rememberCoroutineScope()

    // Vérifie si le cocktail actuellement affiché est déjà dans les favoris
    // Se réinitialise automatiquement quand on change de cocktail (drink?.id)
    val isFav = remember(drink?.id) {
        mutableStateOf(
            drink?.let { sharedPrefs.getFavoriteList().any { fav -> fav.id == drink.id } } ?: false
        )
    }

    CenterAlignedTopAppBar(
        title = { Text("Discover our cocktails") },

        navigationIcon = { // Flèche retour affichée uniquement si onBack est fourni
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            }
        },

        actions = {
            IconToggleButton(
                checked = isFav.value,

                // Quand le user clique sur le coeur: on met a jour l'état visuel, on ajoute/retire le cocktail dans les SharedPreferences et on affiche le message type "notification" pour la confirmation de l'ajout/suppression
                onCheckedChange = {
                    isFav.value = !isFav.value
                    drink?.let {
                        val currentList = sharedPrefs.getFavoriteList()
                        if (isFav.value) currentList.add(drink)
                        else currentList.removeAll { fav -> fav.id == drink.id }
                        sharedPrefs.saveFavoriteList(currentList)
                    }
                    snackbarScope.launch {
                        snackbarHostState.showSnackbar(if (isFav.value) "Added ❤️" else "Removed")
                    }
                }
            ) {
                Icon(
                    imageVector = if (isFav.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "fav"
                )
            }
        }
    )
}
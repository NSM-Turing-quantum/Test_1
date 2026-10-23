package com.example.hometask_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "app_list") {
                        composable("app_list") {
                            AppListScreen(
                                apps = sampleApps,
                                showcaseApps = showcaseApps,
                                onAppClick = { app ->
                                    navController.navigate("app_details/${app.id}")
                                },
                                onShowcaseAppClick = { _ ->
                                    navController.navigate("app_list") {
                                        popUpTo("app_list") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("app_details/{appId}") { backStackEntry ->
                            val appId = backStackEntry.arguments?.getString("appId")?.toIntOrNull()
                            val selectedApp = (sampleApps + showcaseApps).find { it.id == appId }

                            if (selectedApp != null) {
                                AppDetailsScreen(
                                    app = selectedApp,
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

val AccentColor = Color(0xFF6200EE)

data class AppItem(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val icon: ImageVector
)

val sampleApps = listOf(
    AppItem(1, "Camera", "Make foto", "Multimedia", Icons.Default.PhotoCamera),
    AppItem(2, "Galerie", "watch images", "Multimedia", Icons.Default.Image),
    AppItem(3, "Settings", "System usage", "System", Icons.Default.Settings),
    AppItem(4, "Browser", "Internet connection", "Internet", Icons.Default.Language),
    AppItem(5, "Calender", "Meetings", "Productivity", Icons.Default.DateRange)
)

val showcaseApps = listOf(
    AppItem(6, "Music", "Listen to tracks", "Multimedia", Icons.Default.PlayArrow),
    AppItem(7, "Email", "Read and send emails", "Communication", Icons.Default.Email),
    AppItem(8, "Maps", "Find your way", "Navigation", Icons.Default.Place),
    AppItem(9, "Weather", "Check the forecast", "Utilities", Icons.Default.Info),
    AppItem(10, "Notes", "Write down ideas", "Productivity", Icons.Default.Edit),
    AppItem(11, "Calculator", "Solve math problems", "Utilities", Icons.Default.Build),
    AppItem(12, "Clock", "Set alarms", "Utilities", Icons.Default.Refresh),
    AppItem(13, "Contacts", "Manage your contacts", "Communication", Icons.Default.Person),
    AppItem(14, "Files", "Browse your files", "Productivity", Icons.Default.List)
)

@Composable
fun AppListScreen(
    apps: List<AppItem>,
    showcaseApps: List<AppItem>,
    onAppClick: (AppItem) -> Unit,
    onShowcaseAppClick: (AppItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppToolbar()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(apps) { app ->
                AppListItem(app = app, onClick = { onAppClick(app) })
            }
        }

        AppShowcase(
            apps = showcaseApps,
            onAppClick = { app -> onShowcaseAppClick(app) }
        )
    }
}

@Composable
fun AppToolbar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(AccentColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Иконка приложения",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Список приложений",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AppListItem(app: AppItem, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = app.icon,
                contentDescription = null,
                tint = AccentColor,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = app.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = app.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AppShowcase(apps: List<AppItem>, onAppClick: (AppItem) -> Unit) {
    if (apps.isEmpty()) return

    val pageCount = 3
    val itemsPerPage = 3
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Витрина",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 16.dp
        ) { page ->
            val startIndex = page * itemsPerPage
            val pageApps = apps.drop(startIndex).take(itemsPerPage)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pageApps.forEach { app ->
                    Box(modifier = Modifier.weight(1f)) {
                        AppListItem(
                            app = app,
                            onClick = { onAppClick(app) }
                        )
                    }
                }
                repeat(itemsPerPage - pageApps.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsScreen(app: AppItem, onBackClick: () -> Unit) {
    BackHandler(enabled = true) {
        onBackClick()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(app.name) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = app.icon,
                contentDescription = null,
                tint = AccentColor,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = app.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = app.description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Категория: ${app.category}",
                style = MaterialTheme.typography.labelLarge,
                color = AccentColor
            )
        }
    }
}
package com.pizzy.ptilms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.pizzy.ptilms.auth.AuthState
import com.pizzy.ptilms.auth.SessionManager
import com.pizzy.ptilms.data.model.UserRole
import com.pizzy.ptilms.navigation.MainNavGraph
import com.pizzy.ptilms.navigation.Screen
import com.pizzy.ptilms.ui.theme.PTiLMSTheme
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var appInitializer: AppInitializer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appInitializer.initialize(lifecycleScope) { success ->
            if (!success) {
                Timber.e("Database initialization failed")
            }
        }
        setContent {
            PTiLMSTheme {
                val authState by sessionManager.authState.collectAsState(initial = AuthState.Unauthenticated)
                val navController = rememberNavController()

                LaunchedEffect(authState) {
                    when (val state = authState) { // Use smart cast
                        is AuthState.Unauthenticated -> {
                            navController.navigate(Screen.Auth.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }

                        is AuthState.Authenticated -> {
                            val destination = when (state.role) { // Use smart cast
                                UserRole.STUDENT -> Screen.StudentHome.route
                                UserRole.LECTURER -> Screen.LecturerHome.route
                                UserRole.ADMIN -> Screen.Dashboard.route
                            }
                            navController.navigate(destination) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
                MainNavGraph(navController = navController)
            }
        }
    }
}
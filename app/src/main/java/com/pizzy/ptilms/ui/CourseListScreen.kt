package com.pizzy.ptilms.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pizzy.ptilms.ui.components.CoursesList
import com.pizzy.ptilms.ui.components.pulltorefresh.PullRefresh
import com.pizzy.ptilms.ui.components.pulltorefresh.PullRefreshIndicator
import com.pizzy.ptilms.ui.components.pulltorefresh.rememberPullRefreshState
import com.pizzy.ptilms.viewmodel.CourseViewModel
import com.pizzy.ptilms.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    navController: NavHostController,
    courseViewModel: CourseViewModel = hiltViewModel()
) {
    val uiState by courseViewModel.uiStateWithDepartment.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }
    var active by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            courseViewModel.fetchCoursesWithDepartment()
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Course List") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        PullRefresh(
            state = pullRefreshState,
            enabled = true,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        searchQuery = it
                        active = false
                    },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Search courses") },
                    colors = SearchBarDefaults.colors(),
                    leadingIcon = null,
                    trailingIcon = null,
                ) {
                    // Content to display when the search bar is active (expanded)
                }
                CoursesList(
                    uiState = uiState,
                    navController = navController,
                    searchQuery = searchQuery
                )
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success || uiState is UiState.Error || uiState is UiState.Empty) {
            isRefreshing = false
        }
    }
}
package com.kriahsnverma.securevault.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kriahsnverma.securevault.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val passwords by viewModel.passwords.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(title= {Text("SecureVault")})
        },
        floatingActionButton = {
//            FloatingActionButton(onClick = { viewModel.addSample() }) {
//                Text("+")
//            }
        }
    ){ padding ->
        LazyColumn(modifier = Modifier.padding(padding)){
            items(passwords) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(text = entry.title, style = MaterialTheme.typography.titleMedium)
                        Text(text = entry.username, style = MaterialTheme.typography.bodyMedium)
                    }
                }

            }
        }
    }
}
package com.example.list_of_user.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.list_of_user.R

data class User(val id: Int, val name: String, val age: Int, val photoUri: String?)

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun UserListScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("LISTA DE USUÁRIOS") },
                actions = {
                    IconButton(onClick = { /* Handle Add User */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Adicionar Usuário"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val users = remember {
            listOf(
                User(1, "Alice", 34, "https://example.com/photo1.jpg"),
                User(2, "Bob", 29, "https://example.com/photo2.jpg")
            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(users) { user ->
                UserListItem(user)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun UserListItem(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        user.photoUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Foto de ${user.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            Icon(
                painter = painterResource(id = R.drawable.ic_photo),
                contentDescription = "Foto não disponível",
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.Gray),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text("Nome: ${user.name}", style = MaterialTheme.typography.bodyMedium)
            Text("Idade: ${user.age} anos", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

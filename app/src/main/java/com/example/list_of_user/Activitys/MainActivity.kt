package com.example.list_of_user.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.list_of_user.R
import com.example.list_of_user.ui.theme.List_of_userTheme
import java.text.SimpleDateFormat
import java.util.*

data class Item(
    val name: String,
    val birthDate: String,
    val cpf: String,
    val city: String,
    val isActive: Boolean,
    val photoUri: Uri? = null
) {
    val age: Int
        get() {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = format.parse(birthDate)
            val calendar = Calendar.getInstance()
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            return Calendar.getInstance().get(Calendar.YEAR) - year
        }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            List_of_userTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val items = remember {
                        mutableStateListOf(
                            Item("Alice", "01/01/1990", "123.456.789-00", "City A", true),
                            Item("Bob", "02/02/1985", "987.654.321-00", "City B", false)
                        )
                    }

                    MainScreen(
                        items = items,
                        modifier = Modifier.padding(innerPadding),
                        onEdit = { item ->
                            // Handle edit action
                        },
                        onDelete = { item ->
                            items.remove(item)
                        },
                        onAdd = {
                            val intent = Intent(this, RegistrationActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    items: List<Item>,
    modifier: Modifier = Modifier,
    onEdit: (Item) -> Unit,
    onDelete: (Item) -> Unit,
    onAdd: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Usuários") },
                actions = {
                    IconButton(onClick = onAdd) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Adicionar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(items) { item ->
                SwipeableItem(item, onEdit, onDelete)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SwipeableItem(
    item: Item,
    onEdit: (Item) -> Unit,
    onDelete: (Item) -> Unit
) {
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Ajuste a altura conforme necessário
            .background(Color.White)
            .offset { IntOffset(offset.x.toInt(), 0) }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    offset = Offset(
                        x = (offset.x + pan.x).coerceIn(-150f, 150f),
                        y = 0f
                    )
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.LightGray)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item.photoUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Foto de ${item.name}",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Icon(
                    painter = painterResource(id = R.drawable.ic_photo),
                    contentDescription = "Foto não disponível",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text("Nome: ${item.name}")
                Text("Idade: ${item.age} anos")
            }
        }

        if (offset.x > 100) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .background(Color.Green)
                    .clickable {
                        onEdit(item)
                        offset = Offset.Zero
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Editar",
                    tint = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        if (offset.x < -100) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .background(Color.Red)
                    .clickable {
                        onDelete(item)
                        offset = Offset.Zero
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Excluir",
                    tint = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

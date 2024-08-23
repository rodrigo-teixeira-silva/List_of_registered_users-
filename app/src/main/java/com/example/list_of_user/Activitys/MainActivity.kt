package com.example.list_of_user.Activitys

import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import com.example.list_of_user.Adapter.User
import com.example.list_of_user.Database.UserDaoImpl
import com.example.list_of_user.R
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


import com.example.list_of_user.ui.theme.List_of_userTheme
import com.example.list_of_user.utils.calculateAge
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private lateinit var userDao: UserDaoImpl


    private val registrationResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                // Atualizar a lista após a criação do usuário
                var users = loadUsersFromDatabase()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDao = UserDaoImpl(this)

        setContent {
            List_of_userTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var users by remember { mutableStateOf(loadUsersFromDatabase()) }

                    MainScreen(
                        items = users,
                        modifier = Modifier.padding(innerPadding),
                        onEdit = { user ->
                            val intent = Intent(this, EditActivity::class.java).apply {
                                putExtra(
                                    "USER_ID",
                                    user.id
                                ) // Passe o ID do usuário para a EditActivity
                            }
                            startActivity(intent)
                        },
                        onDelete = { user ->
                            userDao.deleteUser(user.id)
                            // Atualizar a lista após a exclusão
                            users = loadUsersFromDatabase()
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

    private fun loadUsersFromDatabase(): List<User> {
        return userDao.getActiveUsers()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    items: List<User>,
    modifier: Modifier = Modifier,
    onEdit: (User) -> Unit,
    onDelete: (User) -> Unit,
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
            items(items) { user ->
                SwipeableUser(user, onEdit, onDelete)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SwipeableUser(
    user: User,
    onEdit: (User) -> Unit,
    onDelete: (User) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem do usuário
            user.photoUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Foto de ${user.name}",
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
                Text("Nome: ${user.name}")
                val age = calculateAge(user.birthDate)
                println("User Birth Date: ${user.birthDate}, Age: $age") // Adicionado para depuração
                Text("Idade: $age anos") // Calcula e exibe a idade
            }
        }

        // Botões de ação no canto direito
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .width(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                iconResId = R.drawable.ic_edit,
                onClick = { onEdit(user) },
                modifier = Modifier
                    .size(30.dp)
            )
            ActionButton(
                iconResId = R.drawable.ic_delete,
                onClick = { onDelete(user) },
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun ActionButton(
    iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null
        )
    }
}

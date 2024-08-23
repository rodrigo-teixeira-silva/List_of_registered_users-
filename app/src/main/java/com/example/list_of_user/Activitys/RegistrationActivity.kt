package com.example.list_of_user.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.list_of_user.Database.UserDatabaseHelper

import com.example.list_of_user.ui.ItemForm
import com.example.list_of_user.ui.theme.List_of_userTheme

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            List_of_userTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Lembrando dos estados para os campos do formulário
                    var name by remember { mutableStateOf("") }
                    var birthDate by remember { mutableStateOf("") }
                    var cpf by remember { mutableStateOf("") }
                    var city by remember { mutableStateOf("") }
                    var isActive by remember { mutableStateOf(true) }
                    var photoUri by remember { mutableStateOf<Uri?>(null) }

                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts
                            .StartActivityForResult()
                    ) { result ->
                        if (result.resultCode == RESULT_OK) {
                            photoUri = result.data?.data
                        }
                    }

                    fun selectImageFromGalery(
                        launcher: ManagedActivityResultLauncher<Intent, androidx.activity.result.ActivityResult>
                    ) {
                        val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        launcher.launch(intent)
                    }

                    val databaseHelper = remember { UserDatabaseHelper(this) }

                    // Implementar o comportamento dos botões e foto
                    val onCreateClick: () -> Unit = {

                        val user = com.example.list_of_user.Adapter.User(
                            name = name,
                            birthDate = birthDate,
                            cpf = cpf,
                            city = city,
                            isActive = isActive,
                            photoUri = photoUri?.toString()
                        )
                        databaseHelper.insertUser(user)
                        setResult(RESULT_OK)
                        finish()
                    }

                    val onPhotoChange: (Uri?) -> Unit = { uri ->
                        photoUri = uri
                    }

                    // Chamando o ItemForm dentro do Scaffold

                    ItemForm(
                        name = name,
                        onNameChange = { name = it },
                        birthDate = birthDate,
                        onBirthDateChange = { birthDate = it },
                        cpf = cpf,
                        onCpfChange = { cpf = it },city = city,
                        onCityChange = { city = it },
                        isActive = isActive,
                        onIsActiveChange = { isActive = it },
                        onCreateClick = onCreateClick,
                        onPickImage = { selectImageFromGalery(launcher) },
                        photoUri = photoUri,
                        onPhotoChange = onPhotoChange, // Add this line
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

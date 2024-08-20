package com.example.list_of_user.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.list_of_user.R

@Composable
fun ItemForm(
    name: String,
    onNameChange: (String) -> Unit,
    birthDate: String,
    onBirthDateChange: (String) -> Unit,
    cpf: String,
    onCpfChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    isActive: Boolean,
    onIsActiveChange: (Boolean) -> Unit,
    onCreateClick: () -> Unit,
    onPhotoChange: (Uri?) -> Unit,
    photoUri: Uri?,
    modifier: Modifier = Modifier,
    onPickImage: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Exibe a imagem se photoUri não for nulo, caso contrário exibe o ícone padrão
        IconButton(
            onClick = onPickImage,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.LightGray)
                .size(100.dp) // Tamanho do círculo
        ) {
            if (photoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(photoUri),
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Crop, // Preenche completamente o espaço disponível
                    modifier = Modifier
                        .size(100.dp) // Mesmo tamanho do círculo
                        .clip(CircleShape) // Mantém a imagem no formato circular
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_photo),
                    contentDescription = "Select Image",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = birthDate,
            onValueChange = onBirthDateChange,
            label = { Text("Birth Date") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = cpf,
            onValueChange = onCpfChange,
            label = { Text("CPF") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = city,
            onValueChange = onCityChange,
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onCreateClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Text("Create")
        }
    }
}


@Preview
@Composable
fun ItemFormPreview() {
    ItemForm(
        name = "John Doe",
        onNameChange = {},
        birthDate = "01/01/1990",
        onBirthDateChange = {},
        cpf = "123.456.789-00",
        onCpfChange = {},
        city = "City A",
        onCityChange = {},
        isActive = true,
        onIsActiveChange = {},
        onCreateClick = {},
        onPhotoChange = {},
        photoUri = null,
        onPickImage = {}
    )
}

package com.example.list_of_user.Adapter

import java.text.SimpleDateFormat
import java.util.*

data class User(
    val id: Int = 0,
    val name: String,
    val birthDate: String,
    val cpf: String,
    val city: String,
    val isActive: Boolean,
    val photoUri: String? = null

) {
    val age: Int
        get() {
            return try {
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = format.parse(birthDate)
                val calendar = Calendar.getInstance()
                calendar.time = date
                val year = calendar.get(Calendar.YEAR)
                Calendar.getInstance().get(Calendar.YEAR) - year
            } catch (e: Exception) {
                // Trate a exceção como necessário, por exemplo, retornando uma idade padrão ou logando o erro
                e.printStackTrace()
                0 // Idade padrão em caso de erro
            }
        }
}
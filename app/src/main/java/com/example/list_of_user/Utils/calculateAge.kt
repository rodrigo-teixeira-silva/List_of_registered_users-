package com.example.list_of_user.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun calculateAge(birthDate: String): Int {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        val birthDateParsed = dateFormat.parse(birthDate) ?: run {
            println("Erro: Data de nascimento não pode ser nula")
            return 0
        }
        val birthCalendar = Calendar.getInstance().apply { time = birthDateParsed }
        val currentCalendar = Calendar.getInstance()

        var age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
        if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        age
    } catch (e: ParseException) {
        e.printStackTrace()
        0
    }
}

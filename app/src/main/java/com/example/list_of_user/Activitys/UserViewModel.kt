package com.example.list_of_user.Activitys
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.list_of_user.Adapter.User
import com.example.list_of_user.Dao.UserDao


import com.example.list_of_user.Database.UserDaoImpl

class UserViewModel(private val userDao: UserDao) : ViewModel() {

//    private val userDao: UserDao = UserDaoImpl(context)

    // LiveData para observação dos dados de usuários
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    init {
        // Carrega os dados quando a ViewModel é criada
        loadUsers()
    }

    private fun loadUsers() {
        // Obtém todos os usuários e atualiza o LiveData
        _users.value = userDao.getAllUsers()
    }

    // Funções adicionais para manipular dados, se necessário
    fun refreshUsers() {
        _users.value = userDao.getAllUsers()
    }
}

package edu.put.gymrathelper

import android.content.Context
import androidx.room.Room
import edu.gymrathelper.database.Account
import edu.put.gymrathelper.database.GymDatabase
import edu.put.gymrathelper.database.Training

class DatabaseHandler(context: Context) {
    private val db = Room.databaseBuilder(
        context,
        GymDatabase::class.java, "gym_database"
    ).build()
    private val gymDao = db.gymDao()

    fun removeAccount(account: Account) {
        gymDao.removeAccount(account.username)
    }

    fun removeTraining(training: Training) {
        gymDao.removeTraining(training.id)
    }

    fun getAllAccounts(): List<Account> {
        return gymDao.getAllAccounts()
    }
    fun insertAccount(account: Account) {
        gymDao.insertAccount(account)
    }

    fun getAccountByLogin(login: String): Account? {
        return gymDao.getAccount(login)
    }

    fun insertTraining(training: Training) {
        gymDao.insertTraining(training)
    }

    fun getAllTrainings(): List<Training> {
        return gymDao.getAllTrainings()
    }

   fun getTrainingById(id: Int): Training {
        return gymDao.getTrainingById(id)
    }

    fun modifyTraining(training: Training) {
        gymDao.modifyTraining(training)
    }

    fun checkIfLoginAttemptIsCorrect(username: String, password: String): Boolean {
        val passwordhash = password.hashCode().toString()
        val account = gymDao.getAccount(username)
        return account != null && account.passwordhash == passwordhash
    }
}

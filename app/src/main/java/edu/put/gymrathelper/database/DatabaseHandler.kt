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

    suspend fun insertAccount(account: Account) {
        gymDao.insertAccount(account)
    }

    suspend fun getAccountByLogin(login: String): Account? {
        return gymDao.getAccount(login)
    }

    suspend fun insertTraining(training: Training) {
        gymDao.insertTraining(training)
    }

    suspend fun getAllTrainings(): List<Training> {
        return gymDao.getAllTrainings()
    }

    suspend fun getTrainingById(id: Int): Training {
        return gymDao.getTrainingById(id)
    }

    suspend fun modifyTraining(training: Training) {
        gymDao.modifyTraining(training)
    }

    suspend fun checkIfLoginAttemptIsCorrect(username: String, password: String): Boolean {
        val passwordhash = password.hashCode().toString()
        val account = gymDao.getAccount(username)
        return account != null && account.passwordhash == passwordhash
    }
}

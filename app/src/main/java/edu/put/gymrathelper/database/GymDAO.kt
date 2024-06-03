package edu.put.gymrathelper.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.gymrathelper.database.Account

@Dao
interface GymDAO {
    @Query("SELECT * FROM accounts WHERE username = :username")
    fun getAccount(username: String): Account

    @Insert
    fun insertAccount(account: Account)

    @Query("SELECT * FROM trainings WHERE id = :id")
    fun getTrainingById(id: Int): Training

    @Query("SELECT * FROM trainings WHERE type = :type")
    fun getTrainingsByType(type: String): List<Training>

    @Query("SELECT * FROM trainings")
    fun getAllTrainings(): List<Training>

    @Insert
    fun insertTraining(training: Training)

    @Update
    fun modifyTraining(training: Training)
}
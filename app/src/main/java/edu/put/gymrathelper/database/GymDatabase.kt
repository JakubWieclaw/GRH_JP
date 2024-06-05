package edu.put.gymrathelper.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.gymrathelper.database.Account

@Database(entities = [Account::class, Training::class], version = 1)
@TypeConverters(ExerciseListConverter::class)
abstract class GymDatabase : RoomDatabase() {
    abstract fun gymDao(): GymDAO
}

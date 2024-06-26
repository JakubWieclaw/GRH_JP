package edu.put.gymrathelper.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "trainings")
data class Training(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val type: String,
    val date: Long,
    val totalTime: Long,
    @TypeConverters(ExerciseListConverter::class)
    val exercises: List<Exercise>
) : Parcelable

@Parcelize
data class Exercise(
    val name: String,
    val weight: String
) : Parcelable

class ExerciseListConverter {
    @TypeConverter
    fun fromExerciseList(exercises: List<Exercise>): String {
        return exercises.joinToString(separator = "|") { "${it.name},${it.weight}" }
    }

    @TypeConverter
    fun toExerciseList(data: String): List<Exercise> {
        return if (data.isEmpty()) {
            emptyList()
        } else {
            data.split("|").map {
                val parts = it.split(",")
                Exercise(parts[0], parts[1])
            }
        }
    }
}
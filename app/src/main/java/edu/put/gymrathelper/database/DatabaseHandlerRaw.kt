import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import edu.put.gymrathelper.database.Account
import edu.put.gymrathelper.database.Training

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "gym_database"
        private const val DATABASE_VERSION = 1

        // Account table
        private const val TABLE_ACCOUNTS = "accounts"
        private const val KEY_ID = "id"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD_HASH = "passwordhash"

        private const val CREATE_ACCOUNTS_TABLE = """
            CREATE TABLE $TABLE_ACCOUNTS (
                $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_USERNAME TEXT UNIQUE,
                $KEY_PASSWORD_HASH TEXT
            )
        """

        // Training table
        private const val TABLE_TRAININGS = "trainings"
        private const val KEY_ID_TRAINING = "id"
        private const val KEY_TYPE = "type"
        private const val KEY_DATE = "date"
        private const val KEY_TOTAL_TIME = "totalTime"
        private const val KEY_EXERCISE = "exercise"
        private const val KEY_WEIGHT = "weight"

        private const val CREATE_TRAININGS_TABLE = """
            CREATE TABLE $TABLE_TRAININGS (
                $KEY_ID_TRAINING INTEGER PRIMARY KEY AUTOINCREMENT,
                $KEY_TYPE TEXT,
                $KEY_DATE INTEGER,
                $KEY_TOTAL_TIME INTEGER,
                $KEY_EXERCISE TEXT,
                $KEY_WEIGHT TEXT
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_ACCOUNTS_TABLE)
        db?.execSQL(CREATE_TRAININGS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TRAININGS")
        onCreate(db)
    }

    fun insertAccount(account: Account) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USERNAME, account.username)
            put(KEY_PASSWORD_HASH, account.passwordhash)
        }
        db.insert(TABLE_ACCOUNTS, null, values)
        db.close()
    }

    fun getAccountByLogin(login: String): Account? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ACCOUNTS, arrayOf(KEY_ID, KEY_USERNAME, KEY_PASSWORD_HASH),
            "$KEY_USERNAME=?", arrayOf(login), null, null, null, null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val account = Account(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_USERNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD_HASH))
            )
            cursor.close()
            db.close()
            account
        } else {
            cursor?.close()
            db.close()
            null
        }
    }

    fun insertTraining(training: Training) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TYPE, training.type)
            put(KEY_DATE, training.date)
            put(KEY_TOTAL_TIME, training.totalTime)
        }
        db.insert(TABLE_TRAININGS, null, values)
        val trainingId = db.rawQuery("SELECT last_insert_rowid()", null).use {
            it.moveToFirst()
            it.getInt(0)
        }
        training.exercises.take(10).forEach { (exercise, weight) ->
            val exerciseValues = ContentValues().apply {
                put(KEY_ID_TRAINING, trainingId)
                put(KEY_EXERCISE, exercise)
                put(KEY_WEIGHT, weight)
            }
            db.insert(TABLE_TRAININGS, null, exerciseValues)
        }
        db.close()
    }

    fun getAllTrainings(): List<Training> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TRAININGS"
        val cursor = db.rawQuery(query, null)
        val trainings = mutableListOf<Training>()
        if (cursor.moveToFirst()) {
            do {
                val training = Training(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_TRAINING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DATE)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(KEY_TOTAL_TIME)),
                    mutableListOf<Pair<String, String>>()
                )
                trainings.add(training)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return trainings
    }

    fun getTrainingById(id: Int): Training? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TRAININGS WHERE $KEY_ID_TRAINING = $id"
        val cursor = db.rawQuery(query, null)
        return if (cursor != null && cursor.moveToFirst()) {
            val training = Training(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID_TRAINING)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE)),
                cursor.getLong(cursor.getColumnIndexOrThrow(KEY_DATE)),
                cursor.getLong(cursor.getColumnIndexOrThrow(KEY_TOTAL_TIME)),
                mutableListOf<Pair<String, String>>()
            )
            cursor.close()
            db.close()
            training
        } else {
            cursor?.close()
            db.close()
            null
        }
    }

    fun modifyTraining(training: Training) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TYPE, training.type)
            put(KEY_DATE, training.date)
            put(KEY_TOTAL_TIME, training.totalTime)
        }
        db.update(TABLE_TRAININGS, values, "$KEY_ID_TRAINING=?", arrayOf(training.id.toString()))
        db.close()
    }

    fun checkIfLoginAttemptIsCorrect(username: String, password: String): Boolean {
        val passwordhash = password.hashCode().toString()
        val account = getAccountByLogin(username)
        return account != null && account.passwordhash == passwordhash
    }
}

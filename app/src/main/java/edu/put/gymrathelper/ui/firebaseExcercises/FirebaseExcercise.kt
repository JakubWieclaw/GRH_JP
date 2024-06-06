package edu.put.gymrathelper.ui.firebaseExcercises

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

data class FirebaseExercise(
    val name: String = "",
    val video: String = "",
    val other_info: String = ""
)

object NetworkClient {
    private val client = OkHttpClient()

    fun fetchExercises(url: String): List<FirebaseExercise> {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body!!.string()
            println("Response: $responseBody")  // Log the response

            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val mapType = Types.newParameterizedType(Map::class.java, String::class.java, FirebaseExercise::class.java)
            val adapter = moshi.adapter<Map<String, FirebaseExercise>>(mapType)

            val exerciseMap = adapter.fromJson(responseBody) ?: emptyMap()
            return exerciseMap.values.toList()
        }
    }
}
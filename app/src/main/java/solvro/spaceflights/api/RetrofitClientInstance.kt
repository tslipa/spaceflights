package solvro.spaceflights.api

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://api.spaceflightnewsapi.net/v3/"
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}
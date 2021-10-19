package solvro.spaceflights.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitDataGetter {
    @get:GET("articles?_limit=30")
    val articles: Call<List<Article>?>?

    @GET("articles/{id}")
    fun getArticle(
        @Path("id") id: String,
    ): Call<Article>
}
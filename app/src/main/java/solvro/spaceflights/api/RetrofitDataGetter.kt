package solvro.spaceflights.api

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitDataGetter {
    @get:GET("articles?_limit=100")
    val articles: Call<List<Article>?>?
}
package solvro.spaceflights.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface GetDataService {
    @get:GET("articles")
    val articles: Call<List<GSONArticle>?>?
}
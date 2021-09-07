package com.nsa.navigation.network

import com.nsa.navigation.models.RecyclerList
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroService {
    @GET("?s=marvel&type=movie&apikey=9446c382")
    suspend fun getDataFromAPI(@Query("page") page:String): RecyclerList
}
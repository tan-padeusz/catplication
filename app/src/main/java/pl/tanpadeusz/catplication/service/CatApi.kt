package pl.tanpadeusz.catplication.service

import pl.tanpadeusz.catplication.data.ImageResponse
import pl.tanpadeusz.catplication.data.VoteRequest
import pl.tanpadeusz.catplication.data.VoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CatApi {
    @GET("/v1/images/search")
    suspend fun getImage(@Query("api_key") apiKey: String, @Query("limit") limit: Int): Response<List<ImageResponse>>

    @POST("/v1/votes")
    suspend fun postVote(@Query("api_key") apiKey: String, @Body voteRequest: VoteRequest): Response<VoteResponse>
}
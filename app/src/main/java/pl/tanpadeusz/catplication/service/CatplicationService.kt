package pl.tanpadeusz.catplication.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import pl.tanpadeusz.catplication.BuildConfig
import pl.tanpadeusz.catplication.data.ImageResponse
import pl.tanpadeusz.catplication.data.VoteRequest
import pl.tanpadeusz.catplication.data.VoteResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CatplicationService private constructor() {
    private val api: CatApi

    init {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create(moshi)).build()
        this.api = retrofit.create(CatApi::class.java)
    }

    suspend fun getImage(): Response<List<ImageResponse>> = this.api.getImage(BuildConfig.API_KEY, 1)
    suspend fun postVote(voteRequest: VoteRequest): Response<VoteResponse> = this.api.postVote(BuildConfig.API_KEY, voteRequest)

    companion object {
        private const val BASE_URL = "https://api.thecatapi.com"
        val instance: CatplicationService by lazy {
            CatplicationService()
        }
    }
}
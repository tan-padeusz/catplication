package pl.tanpadeusz.catplication.data

import com.squareup.moshi.Json

data class VoteRequest(
    @Json(name = "image_id") val imageId: String,
    val value: Int
)

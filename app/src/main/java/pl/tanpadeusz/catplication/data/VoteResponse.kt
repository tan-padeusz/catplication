package pl.tanpadeusz.catplication.data

import com.squareup.moshi.Json

data class VoteResponse(
    @Json(name = "country_code") val countryCode: String,
    val id: Int,
    @Json(name = "image_id") val imageId: String,
    val message: String,
    val value: Int,
)

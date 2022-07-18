package responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    @SerialName("full_size_url") val fullSizeUrl: String
)

package responses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogItem(
    @SerialName("brand_title")
    val brandTitle: String,
    @SerialName("currency")
    val currency: String,
    @SerialName("id")
    val id: Int,
    @SerialName("photo")
    val photo: Photo,
    @SerialName("price")
    val price: String,
    @SerialName("size_title")
    val sizeTitle: String,
    @SerialName("title")
    val title: String,
    @SerialName("url")
    val url: String,
    @SerialName("user")
    val user: User,
)
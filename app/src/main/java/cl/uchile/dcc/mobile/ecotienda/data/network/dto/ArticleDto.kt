package cl.uchile.dcc.mobile.ecotienda.data.network.dto

import cl.uchile.dcc.mobile.ecotienda.data.model.ArticleDetail
import cl.uchile.dcc.mobile.ecotienda.data.model.ArticleSummary
import com.google.gson.annotations.SerializedName

data class ArticleSummaryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("publishedAt") val publishedAt: String? = null,
)

data class ArticleDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("publishedAt") val publishedAt: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("author") val author: String? = null,
)

fun ArticleSummaryDto.toModel() = ArticleSummary(
    id = id,
    title = title,
    summary = summary,
    imageUrl = imageUrl.orEmpty(),
    publishedAt = publishedAt.orEmpty(),
)

fun ArticleDetailDto.toModel() = ArticleDetail(
    id = id,
    title = title,
    summary = summary,
    imageUrl = imageUrl.orEmpty(),
    publishedAt = publishedAt.orEmpty(),
    content = content.orEmpty(),
    author = author.orEmpty(),
)

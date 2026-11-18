package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ArticleDetailDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ArticleSummaryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleApiService {

    @GET("articulos")
    suspend fun getArticles(): List<ArticleSummaryDto>

    @GET("articulos/{articleId}")
    suspend fun getArticle(@Path("articleId") articleId: Int): ArticleDetailDto
}

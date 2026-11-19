package cl.uchile.dcc.mobile.ecotienda.data.network

import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ArticleDetailDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.ArticleSummaryDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.PageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleApiService {

    @GET("articles")
    suspend fun getArticles(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
    ): PageDto<ArticleSummaryDto>

    @GET("articles/{articleId}")
    suspend fun getArticle(@Path("articleId") articleId: Int): ArticleDetailDto
}

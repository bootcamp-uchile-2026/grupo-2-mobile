package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.model.ArticleDetail
import cl.uchile.dcc.mobile.ecotienda.data.model.ArticleSummary
import cl.uchile.dcc.mobile.ecotienda.data.network.ApiRepository
import cl.uchile.dcc.mobile.ecotienda.data.network.ArticleApiService
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.toModel

class ArticleRepository(
    private val apiService: ArticleApiService = ApiRepository.articleApiService,
) {
    suspend fun getArticles(): List<ArticleSummary> {
        return apiService.getArticles().content.map { it.toModel() }
    }

    suspend fun getArticle(articleId: Int): ArticleDetail {
        return apiService.getArticle(articleId).toModel()
    }
}

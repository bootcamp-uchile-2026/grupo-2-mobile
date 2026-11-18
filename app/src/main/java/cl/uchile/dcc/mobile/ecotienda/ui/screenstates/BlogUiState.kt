package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

import cl.uchile.dcc.mobile.ecotienda.data.model.ArticleDetail
import cl.uchile.dcc.mobile.ecotienda.data.model.ArticleSummary

data class BlogUiState(
    val articles: List<ArticleSummary> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val detail: ArticleDetail? = null,
    val detailLoading: Boolean = false,
    val detailError: String? = null,
)

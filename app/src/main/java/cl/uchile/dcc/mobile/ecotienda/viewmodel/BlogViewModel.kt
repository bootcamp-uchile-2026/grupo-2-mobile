package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.repository.ArticleRepository
import cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.BlogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlogViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as? EcoTiendaApp
    private val articleRepository = app?.container?.articleRepository ?: ArticleRepository()

    private val _uiState = MutableStateFlow(BlogUiState())
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = runCatching { articleRepository.getArticles() }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    articles = result.getOrDefault(emptyList()),
                    error = result.exceptionOrNull()?.let { "No pudimos cargar el blog." },
                )
            }
        }
    }

    fun openArticle(articleId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(detailLoading = true, detailError = null, detail = null) }
            val result = runCatching { articleRepository.getArticle(articleId) }
            _uiState.update {
                it.copy(
                    detailLoading = false,
                    detail = result.getOrNull(),
                    detailError = result.exceptionOrNull()?.let { "No pudimos abrir el artículo." },
                )
            }
        }
    }
}

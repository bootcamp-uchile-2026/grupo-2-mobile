package cl.uchile.dcc.mobile.ecotienda.data.model

data class ArticleSummary(
    val id: Int,
    val title: String,
    val summary: String,
    val imageUrl: String,
    val publishedAt: String,
)

data class ArticleDetail(
    val id: Int,
    val title: String,
    val summary: String,
    val imageUrl: String,
    val publishedAt: String,
    val content: String,
    val author: String,
)

fun formatArticleDate(iso: String): String {
    val date = iso.substringBefore('T')
    val parts = date.split('-')
    if (parts.size != 3) return date
    val months = listOf(
        "ene", "feb", "mar", "abr", "may", "jun",
        "jul", "ago", "sep", "oct", "nov", "dic",
    )
    val month = parts[1].toIntOrNull()?.let { months.getOrNull(it - 1) } ?: return date
    val day = parts[2].toIntOrNull() ?: return date
    return "$day $month ${parts[0]}"
}

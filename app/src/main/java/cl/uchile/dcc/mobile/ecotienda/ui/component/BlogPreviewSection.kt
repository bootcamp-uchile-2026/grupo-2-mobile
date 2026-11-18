package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uchile.dcc.mobile.ecotienda.data.model.ArticleSummary
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandTitleStyle
import coil3.compose.SubcomposeAsyncImage

@Composable
fun BlogPreviewSection(
    articles: List<ArticleSummary>,
    isLoading: Boolean,
    onOpenBlog: () -> Unit,
    onOpenArticle: (Int) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Lo último en nuestro blog",
            style = CustomBrandTitleStyle.copy(
                fontSize = 30.sp,
                lineHeight = 36.sp,
                textAlign = TextAlign.Center,
            ),
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Noticias, tips y más...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(22.dp))

        when {
            isLoading && articles.isEmpty() -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            articles.isEmpty() -> {
                Text(
                    text = error ?: "Todavía no hay noticias.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
            else -> {
                val pair = articles.take(2)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    pair.forEach { article ->
                        BlogPreviewCard(
                            article = article,
                            onOpenArticle = onOpenArticle,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                articles.getOrNull(2)?.let { featured ->
                    Spacer(modifier = Modifier.height(18.dp))
                    BlogFeaturedCard(
                        article = featured,
                        onOpenArticle = onOpenArticle,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = onOpenBlog,
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            ),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 12.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ver el Blog",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun BlogImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    showPlay: Boolean = false,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFD5D5D5)),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl.isBlank()) {
            BlogImagePlaceholder(Modifier.fillMaxSize())
        } else {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = { BlogImagePlaceholder(Modifier.fillMaxSize()) },
                error = { BlogImagePlaceholder(Modifier.fillMaxSize()) },
            )
        }
        if (showPlay) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = Color(0xFF3A3A3A),
                modifier = Modifier.size(48.dp),
            )
        }
    }
}

@Composable
private fun BlogPreviewCard(
    article: ArticleSummary,
    onOpenArticle: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clickable { onOpenArticle(article.id) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BlogImage(
            imageUrl = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(168.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = article.summary,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        ReadMoreLink(onClick = { onOpenArticle(article.id) })
    }
}

@Composable
private fun BlogFeaturedCard(
    article: ArticleSummary,
    onOpenArticle: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenArticle(article.id) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BlogImage(
            imageUrl = article.imageUrl,
            contentDescription = article.title,
            showPlay = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = article.summary,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(8.dp))
        ReadMoreLink(onClick = { onOpenArticle(article.id) })
    }
}

@Composable
private fun ReadMoreLink(onClick: () -> Unit) {
    Text(
        text = "Leer más",
        style = MaterialTheme.typography.bodyMedium.copy(
            textDecoration = TextDecoration.Underline,
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Composable
private fun BlogImagePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(56.dp),
            )
        }
    }
}

package solvro.spaceflights.database

import solvro.spaceflights.api.Article

class DatabaseUtils {
    companion object {
        fun toArticle(entity: Entity): Article {
            val article = Article()

            article.id = entity.id
            article.title = entity.title
            article.url = entity.url
            article.imageUrl = entity.imageUrl
            article.newsSite = entity.newsSite
            article.summary = entity.summary
            article.publishedAt = entity.publishedAt
            article.updatedAt = entity.updatedAt

            return article
        }

        fun toEntity(article: Article, favourite: Boolean): Entity {
            return Entity(
                article.id,
                article.title,
                article.url,
                article.imageUrl,
                article.newsSite,
                article.summary,
                article.publishedAt,
                article.updatedAt,
                favourite
            )
        }
    }
}
package solvro.spaceflights.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Entity(
    @PrimaryKey val id: Int,
    val title: String?,
    val url: String?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "news_site") val newsSite: String?,
    val summary: String?,
    @ColumnInfo(name = "published_at") val publishedAt: String?,
    @ColumnInfo(name = "updated_at") val updatedAt: String?,
    var favourite: Boolean
)
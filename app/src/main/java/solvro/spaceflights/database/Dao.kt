package solvro.spaceflights.database

import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {
    @Query("SELECT * FROM entity ORDER BY id DESC")
    fun getAll(): List<Entity>

    @Query("SELECT * FROM entity WHERE favourite = 1 ORDER BY id DESC")
    fun getFavourite(): List<Entity>

    @Query("SELECT * FROM entity WHERE id = :id")
    fun getEntity(id: Int): List<Entity>

    @Query("UPDATE entity SET favourite = :favourite WHERE id = :id")
    fun setIfFavourite(id: Int, favourite: Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEntity(entity: Entity)

    @Query("DELETE FROM entity WHERE id = :id")
    fun removeEntity(id: Int)

    @Query("SELECT * FROM entity WHERE title LIKE :text")
    fun getFromQuery(text: String): List<Entity>
}

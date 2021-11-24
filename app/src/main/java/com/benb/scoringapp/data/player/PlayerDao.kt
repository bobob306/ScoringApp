package com.benb.scoringapp.data.player

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player)

    @Update
    suspend fun update(player: Player)

    @Delete
    suspend fun delete(player: Player)

    @Query ("SELECT * FROM players WHERE id = :id" )
    fun getPlayer(id: Int): Flow<Player>

    @Query ("SELECT * FROM players ORDER BY id ASC")
    fun getPlayers() : Flow<List<Player>>

    @Query("SELECT name FROM players ORDER BY id ASC")
    fun getPlayerNames() : Flow<MutableList<String>>
}
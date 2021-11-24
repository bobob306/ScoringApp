package com.benb.scoringapp.data.score

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(score: Score)

    @Update
    suspend fun update(score: Score)

    @Delete
    suspend fun delete(score: Score)

    @Query ("SELECT * FROM scores WHERE id = :id")
    fun getScore(id: Int): Flow<Score>

    @Query ("SELECT round FROM scores ORDER BY id ASC")
    fun getRoundNumbers(): Flow<List<Int>>

    @Query("SELECT `tricks in round` FROM scores ORDER BY id ASC")
    fun getTricksInRound() : Flow<List<Int>>

    @Query("SELECT `starting player` FROM scores ORDER BY id ASC")
    fun getStartingPlayer() : Flow<List<Int>>
}
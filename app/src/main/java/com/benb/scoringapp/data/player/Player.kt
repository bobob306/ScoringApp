package com.benb.scoringapp.data.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "players")
data class Player(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo (name = "name")
    val playerName: String,
    @ColumnInfo (name = "bid")
    val playerBid: Int = 0,
    @ColumnInfo (name = "tricks")
    val playerTricks: Int = 0,
    @ColumnInfo (name = "score")
    val playerScore: Int = 0
)

package com.benb.scoringapp.data.score

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo (name = "round")
    val roundNumber: Int = id,
    @ColumnInfo (name = "tricks in round")
    val tricksInRound: Int,
    @ColumnInfo (name = "starting player")
    val startingPlayer: Int,
    @ColumnInfo (name = "p0bid")
    val p0bids: Int = 0,
    @ColumnInfo (name = "p0tricks")
    val p0tricks: Int = 0,
    @ColumnInfo (name = "p0score")
    val p0scores: Int = 0,
    @ColumnInfo (name = "p1bid")
    val p1bids: Int = 0,
    @ColumnInfo (name = "p1tricks")
    val p1tricks: Int = 0,
    @ColumnInfo (name = "p1score")
    val p1scores: Int = 0,
    @ColumnInfo (name = "p2bid")
    val p2bids: Int = 0,
    @ColumnInfo (name = "p2tricks")
    val p2tricks: Int = 0,
    @ColumnInfo (name = "p2score")
    val p2scores: Int = 0,
    @ColumnInfo (name = "p3bid")
    val p3bids: Int = 0,
    @ColumnInfo (name = "p3tricks")
    val p3tricks: Int = 0,
    @ColumnInfo (name = "p3score")
    val p3scores: Int = 0,
    @ColumnInfo (name = "p4bid")
    val p4bids: Int = 0,
    @ColumnInfo (name = "p4tricks")
    val p4tricks: Int = 0,
    @ColumnInfo (name = "p4score")
    val p4scores: Int = 0,
    @ColumnInfo (name = "p5bid")
    val p5bids: Int = 0,
    @ColumnInfo (name = "p5tricks")
    val p5tricks: Int = 0,
    @ColumnInfo (name = "p5score")
    val p5scores: Int = 0,

)

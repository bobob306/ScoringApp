package com.benb.scoringapp

import android.app.Application
import com.benb.scoringapp.data.player.PlayerRoomDatabase

class ScoringApplication: Application() {

    val database: PlayerRoomDatabase by lazy { PlayerRoomDatabase.getDatabase(this) }
}
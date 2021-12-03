package com.benb.scoringapp.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.benb.scoringapp.data.player.Player
import com.benb.scoringapp.data.player.PlayerDao
import kotlinx.coroutines.launch

class PlayerViewModel(private val playerDao: PlayerDao) : ViewModel() {

    private val _state = MutableLiveData<String>("SetPlayers")
    val state: LiveData<String>
        get() = _state

    private val _roundNumber = MutableLiveData<MutableList<Int>>()
    val roundNumber: LiveData<MutableList<Int>>
        get() = _roundNumber

    private val _totalTricks = MutableLiveData<MutableList<Int>>()
    val totalTricks: LiveData<MutableList<Int>>
        get() = _totalTricks

    private val _biddableTricks = MutableLiveData<MutableList<Int>>()
    val biddableTricks: LiveData<MutableList<Int>>
        get() = _biddableTricks

    private val _starter = MutableLiveData<MutableList<Int>>()
    val starter: LiveData<MutableList<Int>>
        get() = _starter

    private val _roundNumberString = MutableLiveData<String>()
    val roundNumberString: LiveData<String>
        get() = _roundNumberString

    private val _roundCounter = MutableLiveData<Int>()
    val roundCounter: LiveData<Int>
        get() = _roundCounter

    val allPlayers: LiveData<List<Player>> = playerDao.getPlayers().asLiveData()

    var numberOfRounds = 0

    private val _firstPlayerId = MutableLiveData<Int>()
    val firstPlayerId: LiveData<Int> = _firstPlayerId

    private val _lastPlayerId = MutableLiveData<Int>()
    val lastPlayerId: LiveData<Int> = _lastPlayerId

    /*
    No need for this line anymore
    var roundNumberStrings: String =
        "Round number ${_roundNumber.value?.get(numberOfRounds) ?: 0} of ${_roundNumber.value?.last() ?: 0}"

     */

    private fun insertPlayer(player: Player) {
        viewModelScope.launch {
            playerDao.insert(player)
        }
    }

    fun retrievePlayer(id: Int): LiveData<Player> {
        return playerDao.getPlayer(id).asLiveData()
    }

    fun addNewPlayer(playerName: String) {
        val newPlayer = getNewPlayerEntry(playerName)
        insertPlayer(newPlayer)
    }

    fun setLastPlayer() {
        val numberOfPlayers = allPlayers.value!!.size - 1

        _firstPlayerId.value = if (_firstPlayerId.value!! > numberOfPlayers) {
            0
        } else {
            _firstPlayerId.value
        }

        _lastPlayerId.value = ((firstPlayerId.value!!) - 1)
        if (_firstPlayerId.value == 0) {
            _lastPlayerId.value = numberOfPlayers
        }
        //updateTVs()
    }


    private fun getNewPlayerEntry(playerName: String): Player {
        return Player(
            playerName = playerName
        )
    }

    fun getPlayerNameList(): LiveData<MutableList<String>> {
        val playerNameList = playerDao.getPlayerNames().asLiveData()
        return playerNameList
    }

    private fun updateRoundString() {
        _roundNumberString.value =
            "Round number ${_roundNumber.value?.get(roundCounter.value ?: 0) ?: 0} of ${_roundNumber.value?.last() ?: 0}"
    }

    /*
    private fun updateTVs() {
        _firstPlayerId.value = firstPlayerId.value
        _lastPlayerId.value = lastPlayerId.value
    }
    */

    fun setBid() {

    }

    fun nextState() {
        numberOfRounds++
        when (_state.value) {
            "SetPlayers" -> {
                _state.value = "TakeBid"
                numberOfRounds = allPlayers.value?.size ?: 0
                _roundCounter.value = 0
                _firstPlayerId.value = 0
                createRounds()
                createTrickList()
                setLastPlayer()
                updateRoundString()
                Log.d(
                    "last player",
                    allPlayers.value?.get(lastPlayerId.value ?: 0)?.playerName.toString()
                )
                Log.d(
                    "first player",
                    allPlayers.value?.get(firstPlayerId.value ?: 0)?.playerName.toString()
                )

            }
            "TakeBid" -> {
                _state.value = "TakeTricks"
            }
            "TakeTricks" -> {
                updateRoundString()
                _state.value = "TakeBid"
                _firstPlayerId.value = _firstPlayerId.value!!.plus(1)
                _roundCounter.value = _roundCounter.value!!.plus(1)
                setLastPlayer()
                updateRoundString()
                Log.d(
                    "last player",
                    allPlayers.value?.get(lastPlayerId.value ?: 0)?.playerName.toString()
                )
                Log.d(
                    "first player",
                    allPlayers.value?.get(firstPlayerId.value ?: 0)?.playerName.toString()
                )
            }
        }
    }

    fun deletePlayer(player: Player) {
        viewModelScope.launch {
            playerDao.delete(player)
        }
    }

    fun createRounds() {
        val roundList = mutableListOf<Int>()
        for (i in 1..(allPlayers.value?.size!! * 4)) {
            roundList.add(i)
            _roundNumber.value = roundList
        }

    }

    fun createTrickList() {
        val trickList = mutableListOf<Int>()
        repeat(allPlayers.value?.size!!) {
            trickList.add(2)
        }
        repeat(allPlayers.value?.size!!) {
            trickList.add(4)
        }
        repeat(allPlayers.value?.size!!) {
            trickList.add(6)
        }
        repeat(allPlayers.value?.size!!) {
            trickList.add(8)
        }
        _totalTricks.value = trickList
        _roundNumberString.value =
            ("Round number ${_roundNumber.value?.get(roundCounter.value ?: 0) ?: 0} of ${_roundNumber.value?.last() ?: 0}")
    }


}


class PlayerViewModelFactory(private val playerDao: PlayerDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(playerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
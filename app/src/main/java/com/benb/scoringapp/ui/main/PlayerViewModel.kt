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

    var roundNumberStrings: String =
        "Round number ${_roundNumber.value?.get(numberOfRounds) ?: 0} of ${_roundNumber.value?.last() ?: 0}"

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

    fun nextState() {
        numberOfRounds++
        when (_state.value) {
            "SetPlayers" -> {
                Log.d("state", _state.value.toString())
                _state.value = "TakeBid"
                Log.d("state", _state.value.toString())
                numberOfRounds = allPlayers.value?.size ?: 0
                _roundCounter.value = 0
                createRounds()
                createTrickList()
                Log.d("Round Number", _roundNumber.value?.get(_roundCounter.value!!).toString())
                Log.d("Last Round Number", _roundNumber.value?.last().toString())
                Log.d("Total Tricks", _totalTricks.value?.get(_roundCounter.value!!).toString())
                Log.d("RNS", roundNumberString.value.toString())

            }
            "TakeBid" -> {
                _roundCounter.value = _roundCounter.value!!.plus(1)
                updateRoundString()
                Log.d("state", _state.value.toString())
                _state.value = "TakeTricks"
                Log.d("state", _state.value.toString())
                Log.d("state", _roundCounter.value.toString())
                Log.d("Round Number", _roundNumber.value?.get(_roundCounter.value!!).toString())
                Log.d("Last Round Number", _roundNumber.value?.last().toString())
                Log.d("Total Tricks", _totalTricks.value?.get(_roundCounter.value!!).toString())
                Log.d(
                    "RNS",
                    "Round number ${_roundNumber.value?.get(roundCounter.value ?: 0) ?: 0} of ${_roundNumber.value?.last() ?: 0}"
                )
                Log.d("RNS", _roundNumberString.value.toString())
            }
            "TakeTricks" -> {
                updateRoundString()
                Log.d("state", _state.value.toString())
                _state.value = "TakeBid"
                Log.d("state", _state.value.toString())
                Log.d("state", _roundCounter.value.toString())
                Log.d("Round Number", _roundNumber.value?.get(_roundCounter.value!!).toString())
                Log.d("Last Round Number", _roundNumber.value?.last().toString())
                Log.d("Total Tricks", _totalTricks.value?.get(_roundCounter.value!!).toString())
                Log.d(
                    "RNS",
                    "Round number ${_roundNumber.value?.get(roundCounter.value ?: 0) ?: 0} of ${_roundNumber.value?.last() ?: 0}"
                )
                Log.d("RNS", _roundNumberString.value.toString())
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
        _roundNumberString.value = ("Round number ${_roundNumber.value?.get(roundCounter.value ?: 0) ?: 0} of ${_roundNumber.value?.last() ?: 0}")
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
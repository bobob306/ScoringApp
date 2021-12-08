package com.benb.scoringapp.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benb.scoringapp.PlayerListAdapter
import com.benb.scoringapp.R
import com.benb.scoringapp.ScoringApplication
import com.benb.scoringapp.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: PlayerViewModel by activityViewModels {
        PlayerViewModelFactory(
            (activity?.application as ScoringApplication).database.playerDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        nextButton()

        val bids = resources.getStringArray(R.array.bids)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, bids)
        binding.autoCompleteTextViewBid.setAdapter(arrayAdapter)
        binding.autoCompleteTextViewTrick.setAdapter(arrayAdapter)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            mainFragment = this@MainFragment
            roundNumber.text =
                "Round number ${sharedViewModel.roundNumber.value?.get(sharedViewModel.roundCounter.value ?: 0) ?: 0} of ${sharedViewModel.roundNumber.value?.last() ?: 0}"
        }

        //binding.roundNumber.text = "Round number ${sharedViewModel.roundNumber.value?.get(sharedViewModel.roundCounter.value ?: 0) ?: 0} of ${sharedViewModel.roundNumber.value?.last() ?: 0}"

        val adapter = PlayerListAdapter {
            val action = MainFragmentDirections.actionMainFragmentToBidFragment(it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter

        sharedViewModel.allPlayers.observe(this.viewLifecycleOwner) { players ->
            players.let {
                adapter.submitList(it)
            }
        }



        binding.recyclerView.layoutManager =
            GridLayoutManager(this.context, 1, RecyclerView.VERTICAL, false)

        //binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.addPlayer.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToAddPlayerFragment()
            this.findNavController().navigate(action)
        }



        binding.next.setOnClickListener {
            sharedViewModel.nextState()
            nextButton()
        }

    }

    fun nextButton() {
        when (sharedViewModel.state.value) {
            "SetPlayers" -> {
                binding.firstPlayerTV.text = "First player: "
                binding.lastPlayerTV.text = "Last player: "
                binding.playerNameAction.text = " bids:"
                binding.addPlayer.isVisible = true
                binding.playerNameAction.isVisible = false
                binding.autoCompleteTextViewBid.isVisible = false
                binding.textInputLayout.isVisible = false
                binding.textInputLayoutTrick.isVisible = false
                binding.autoCompleteTextViewTrick.isVisible = false
            }
            "TakeBid" -> {
                binding.firstPlayerTV.text =
                    "First player: ${sharedViewModel.allPlayers.value!![sharedViewModel.firstPlayerId.value!!].playerName}"
                binding.lastPlayerTV.text =
                    "Last player: ${sharedViewModel.allPlayers.value!![sharedViewModel.lastPlayerId.value!!].playerName}"
                binding.playerNameAction.text =
                    "${sharedViewModel.allPlayers.value!![sharedViewModel.firstPlayerId.value!!].playerName} bids:"
                binding.addPlayer.isVisible = false
                binding.playerNameAction.isVisible = true
                binding.textInputLayoutTrick.isVisible = false
                binding.autoCompleteTextViewBid.isVisible = true
                binding.textInputLayout.isVisible = true
                binding.autoCompleteTextViewTrick.isVisible = false
            }
            "TakeTricks" -> {
                binding.firstPlayerTV.text =
                    "First player: ${sharedViewModel.allPlayers.value!![sharedViewModel.firstPlayerId.value!!].playerName}"
                binding.lastPlayerTV.text =
                    "Last player: ${sharedViewModel.allPlayers.value!![sharedViewModel.lastPlayerId.value!!].playerName}"
                binding.playerNameAction.text =
                    "${sharedViewModel.allPlayers.value!![sharedViewModel.firstPlayerId.value!!].playerName} bids:"
                binding.addPlayer.isVisible = false
                binding.autoCompleteTextViewBid.isVisible = false
                binding.autoCompleteTextViewTrick.isVisible = true
                binding.textInputLayout.isVisible = false
                binding.textInputLayoutTrick.isVisible = true
            }
        }
    }

}
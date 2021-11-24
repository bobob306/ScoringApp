package com.benb.scoringapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benb.scoringapp.PlayerListAdapter
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            mainFragment = this@MainFragment
            roundNumber.text = sharedViewModel.roundNumberString.toString()
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
                binding.addPlayer.isVisible = true
            }
            "TakeBid" -> {
                binding.addPlayer.isVisible = false
            }
            "TakeTricks" -> {
                binding.addPlayer.isVisible = false
            }
        }
    }

}
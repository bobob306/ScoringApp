package com.benb.scoringapp.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.benb.scoringapp.ScoringApplication
import com.benb.scoringapp.data.player.Player
import com.benb.scoringapp.databinding.FragmentBidBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * A simple [Fragment] subclass.
 * Use the [BidFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BidFragment : Fragment() {

    private val navigationArgs: BidFragmentArgs by navArgs()

    private val viewModel: PlayerViewModel by activityViewModels {
        PlayerViewModelFactory(
            (activity?.application as ScoringApplication).database.playerDao()
        )
    }

    lateinit var player: Player
    private var _binding: FragmentBidBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.playerId
        viewModel.retrievePlayer(id).observe(this.viewLifecycleOwner) { selectedPlayer ->
            player = selectedPlayer
            bind(player)
        }
    }

    private fun bind (player: Player) {
        binding.apply {
            playerName.text = player.playerName
            playerScore.text = player.playerScore.toString()
            deleteButton.setOnClickListener { showConfirmationDialogue() }
        }
    }

    private fun showConfirmationDialogue() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage("Are you sure you want to delete this player?")
            .setCancelable(false)
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                deletePlayer()
            }
            .show()
    }

    private fun deletePlayer(){
        viewModel.deletePlayer(player)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}
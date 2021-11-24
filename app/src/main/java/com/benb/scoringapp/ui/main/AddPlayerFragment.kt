package com.benb.scoringapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.benb.scoringapp.ScoringApplication
import com.benb.scoringapp.data.player.Player
import android.content.Context.INPUT_METHOD_SERVICE
import androidx.navigation.fragment.findNavController
import android.view.inputmethod.InputMethodManager
import com.benb.scoringapp.databinding.FragmentAddPlayerBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddPlayerFragment : Fragment() {

    private val viewModel: PlayerViewModel by activityViewModels {
        PlayerViewModelFactory(
            (activity?.application as ScoringApplication).database.playerDao()
        )
    }

    lateinit var player: Player

    private var _binding: FragmentAddPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveAction.setOnClickListener {
            addNewPlayer()
        }
    }

    private fun addNewPlayer() {
        viewModel.addNewPlayer(
            binding.playerName.text.toString()
        )
        val action = AddPlayerFragmentDirections.actionAddPlayerFragmentToMainFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}
package com.benb.scoringapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.benb.scoringapp.data.player.Player
import com.benb.scoringapp.databinding.PlayerListItemBinding

class PlayerListAdapter(private val onItemClicked: (Player) -> Unit) :
    ListAdapter<Player, PlayerListAdapter.PlayerViewHolder>(DiffCallback)
{
    class PlayerViewHolder(private var binding: PlayerListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind (player: Player) {
                binding.apply {
                    playerName.text = player.playerName
                }
            }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Player>() {
            override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
                return oldItem.playerName ==  newItem.playerName
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerListAdapter.PlayerViewHolder {
        return PlayerViewHolder(
            PlayerListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: PlayerListAdapter.PlayerViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onItemClicked(current)
        }
        holder.bind(current)
    }
}
package com.ricardosfp.zapping.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ricardosfp.zapping.databinding.ZappingItemBinding
import com.ricardosfp.zapping.domain.model.Match
import java.text.SimpleDateFormat
import java.util.Locale

class ZappingDayAdapter: RecyclerView.Adapter<ZappingDayAdapter.ViewHolder>() {
    private var zappingList = listOf<Match>()

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    }

    fun setItems(list: List<Match>) {
        zappingList = list
        notifyDataSetChanged()
    }

    class ViewHolder(private var itemBinding: ZappingItemBinding):
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(match: Match) {
            itemBinding.zappingMatch.text = String.format("%s x %s", match.homeTeam, match.awayTeam)
            itemBinding.zappingDate.text = DATE_FORMAT.format(match.date)
            itemBinding.zappingChannel.text = match.channel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ZappingItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = zappingList[position]
        holder.bind(match)
    }

    override fun getItemCount(): Int {
        return zappingList.size
    }
}
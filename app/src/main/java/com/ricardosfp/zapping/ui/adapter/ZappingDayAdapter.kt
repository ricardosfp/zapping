package com.ricardosfp.zapping.ui.adapter

import android.view.*
import androidx.recyclerview.widget.*
import com.ricardosfp.zapping.databinding.*
import com.ricardosfp.zapping.domain.model.*
import java.text.*
import java.util.*

class ZappingDayAdapter: RecyclerView.Adapter<ZappingDayAdapter.ViewHolder>() {
    private var zappingList: ArrayList<Match>

    init {
        zappingList = ArrayList()
    }

    companion object {
        private val dateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    }

    fun setItems(list: ArrayList<Match>) {
        (list.clone() as? ArrayList<Match>)?.also {
            zappingList = it
            notifyDataSetChanged()
        }
    }

    class ViewHolder(private var itemBinding: ZappingItemBinding):
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(match: Match) {
            itemBinding.zappingMatch.text = String.format("%s x %s", match.homeTeam, match.awayTeam)
            itemBinding.zappingDate.text = dateFormat.format(match.date)
            itemBinding.zappingChannel.text = match.channel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ZappingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
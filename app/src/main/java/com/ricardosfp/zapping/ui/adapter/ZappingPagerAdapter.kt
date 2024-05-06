package com.ricardosfp.zapping.ui.adapter

import androidx.fragment.app.*
import androidx.viewpager2.adapter.*
import com.ricardosfp.zapping.domain.model.*
import com.ricardosfp.zapping.ui.view.*
import java.util.*

class ZappingPagerAdapter(fragment: Fragment, private var map: TreeMap<Date, ArrayList<Match>>):
    FragmentStateAdapter(fragment) {
    private var mapEntryList: List<Map.Entry<Date, ArrayList<Match>>> = ArrayList<Map.Entry<Date, ArrayList<Match>>>(map.entries)

    override fun createFragment(position: Int): Fragment {
        return ZappingDayFragment.newInstance(mapEntryList[position].value)
    }

    override fun getItemCount(): Int {
        return map.size
    }

}
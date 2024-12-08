package com.ricardosfp.zapping.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ricardosfp.zapping.domain.model.Match
import com.ricardosfp.zapping.ui.view.ZappingDayFragment
import java.util.Date

class ZappingPagerAdapter(fragment: Fragment, private var map: Map<Date, List<Match>>):
    FragmentStateAdapter(fragment) {

    private val mapEntryList = map.entries.toList()

    override fun createFragment(position: Int): Fragment {
        return ZappingDayFragment.newInstance(mapEntryList[position].value)
    }

    override fun getItemCount(): Int {
        return map.size
    }
}
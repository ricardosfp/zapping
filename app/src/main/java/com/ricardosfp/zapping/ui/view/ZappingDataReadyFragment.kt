package com.ricardosfp.zapping.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ricardosfp.zapping.databinding.FragmentZappingDataReadyBinding
import com.ricardosfp.zapping.domain.model.Match
import com.ricardosfp.zapping.ui.adapter.ZappingPagerAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ZappingDataReadyFragment: Fragment() {
    private lateinit var viewBinding: FragmentZappingDataReadyBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentZappingDataReadyBinding.inflate(inflater, container, false)
        viewPager = viewBinding.viewPager

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dayMap = (arguments?.getSerializable(DAY_MAP_KEY) as? Map<Date, List<Match>>) ?: return

        // now that we have the matches assigned to days we can instantiate the FragmentStateAdapter
        // maybe unregister this observer?
        viewPager.adapter = ZappingPagerAdapter(this, dayMap)

        val mapEntryList = dayMap.entries.toList()

        // todo move date formatting to the DateConverter
        TabLayoutMediator(
            viewBinding.tabLayout,
            viewPager) { tab: TabLayout.Tab, position: Int ->
            tab.text = DATE_FORMAT.format(mapEntryList[position].key)
        }.attach()
    }

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH)
        const val DAY_MAP_KEY = "DAY_MAP_KEY"

        fun newInstance(dayMap: Map<Date, List<Match>>): ZappingDataReadyFragment {
            val fragment = ZappingDataReadyFragment()
            val bundle = Bundle()
            bundle.putSerializable(DAY_MAP_KEY, LinkedHashMap(dayMap))
            fragment.arguments = bundle
            return fragment
        }
    }
}
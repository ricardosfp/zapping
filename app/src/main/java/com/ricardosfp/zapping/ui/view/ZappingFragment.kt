package com.ricardosfp.zapping.ui.view

import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.viewpager2.widget.*
import com.google.android.material.snackbar.*
import com.google.android.material.tabs.*
import com.ricardosfp.zapping.R
import com.ricardosfp.zapping.databinding.*
import com.ricardosfp.zapping.ui.adapter.*
import com.ricardosfp.zapping.ui.viewmodel.zapping.*
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.*
import dagger.hilt.android.*
import java.text.*
import java.util.*

@AndroidEntryPoint
class ZappingFragment: Fragment() {
    private lateinit var viewBinding: FragmentZappingBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var viewModel: ZappingViewModel

    companion object {
        private val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.ENGLISH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this)[ZappingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentZappingBinding.inflate(inflater, container, false)
        viewPager = viewBinding.viewPager

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // todo this means that data is not fetched again when recovering from process death.
        //  Implement a cache mechanism. See (refresh = false)
        if (savedInstanceState == null) {
            viewModel.getMatches()
        }
        // this has to be done here with the view lifecycle to avoid a strange situation (for example,
        // when having a FragmentTransaction to another Fragment and then popping the back stack).
        // In that case the view gets destroyed but the Fragment itself does not get destroyed.
        // So, in that case, the Observer would not be called again
        viewModel.matchesLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is GetMatchesSuccess -> {
                    Snackbar.make(
                        viewBinding.fragmentRssCoordinatorLayout,
                        R.string.match_list_update_success,
                        BaseTransientBottomBar.LENGTH_LONG)
                            .show()

                    // now that we have the matches assigned to days we can instantiate the FragmentStateAdapter
                    // maybe unregister this observer?
                    viewPager.adapter = ZappingPagerAdapter(this, response.dayMap)

                    val mapEntryList = response.dayMap.entries.toList()

                    // todo move date formatting to the DateConverter
                    TabLayoutMediator(
                        viewBinding.tabLayout,
                        viewPager) { tab: TabLayout.Tab, position: Int ->
                        tab.text = dateFormat.format(mapEntryList[position].key)
                    }.attach()

//                    mapEntryList.forEach { dateArrayListEntry ->
//                        dateArrayListEntry.value.forEach { match -> // schedule an alarm for that time
//                            val text = match.originalText
//                            val date = match.date
//
//                            // todo should this be done here? I don't think so
//                            viewModel.scheduleAlarm(Alarm(text, date))
//                        }
//                    }
                }

                is GetMatchesError -> {
                    // show some error. Do it here or leave it to one of the DayFragment
                    Snackbar.make(
                        viewBinding.fragmentRssCoordinatorLayout,
                        R.string.match_list_update_error,
                        BaseTransientBottomBar.LENGTH_LONG)
                            .show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.update -> // go get the RSS feed
                viewModel.getMatches()

            else -> return false
        }
        return true
    }

}
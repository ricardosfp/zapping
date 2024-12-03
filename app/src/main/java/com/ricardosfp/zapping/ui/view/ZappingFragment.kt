package com.ricardosfp.zapping.ui.view

import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import com.ricardosfp.zapping.R
import com.ricardosfp.zapping.databinding.*
import com.ricardosfp.zapping.ui.view.ZappingDataReadyFragment.Companion.DAY_MAP_KEY
import com.ricardosfp.zapping.ui.viewmodel.zapping.*
import com.ricardosfp.zapping.ui.viewmodel.zapping.model.*
import dagger.hilt.android.*

@AndroidEntryPoint
class ZappingFragment: Fragment() {
    private lateinit var viewBinding: FragmentZappingBinding
    private lateinit var viewModel: ZappingViewModel

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
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                UiIdle, UiLoading -> {
                    childFragmentManager.beginTransaction().replace(
                        viewBinding.zappingFragmentContainer.id,
                        ZappingLoadingFragment::class.java,
                        null)
                            .commit()
                }

                is UiDataReady -> {

                    // todo if the fragment already exists then maybe we should not replace it
                    if (savedInstanceState == null) {
                        val bundle = Bundle()
                        bundle.putSerializable(DAY_MAP_KEY, LinkedHashMap(response.dayMap))

                        childFragmentManager.beginTransaction().replace(
                            viewBinding.zappingFragmentContainer.id,
                            ZappingDataReadyFragment::class.java,
                            bundle)
                                .commit()
                    }

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

                is UiError -> {
                    // show some error. Do it here or leave it to one of the DayFragment
                    childFragmentManager.beginTransaction().replace(
                        viewBinding.zappingFragmentContainer.id,
                        ZappingErrorFragment::class.java,
                        null)
                            .commit()
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
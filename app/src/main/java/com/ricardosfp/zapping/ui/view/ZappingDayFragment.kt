package com.ricardosfp.zapping.ui.view

import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.recyclerview.widget.*
import com.ricardosfp.zapping.databinding.*
import com.ricardosfp.zapping.domain.model.*
import com.ricardosfp.zapping.ui.adapter.*

class ZappingDayFragment: Fragment() {
    private lateinit var viewBinding: FragmentRssBinding
    private lateinit var adapter: ZappingDayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        viewBinding = FragmentRssBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @SuppressWarnings("unchecked")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ZappingDayAdapter()
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        // como resolver este Unchecked Cast ?
        (arguments?.getSerializable(MATCHES_KEY) as? ArrayList<Match>)?.also {
            adapter.setItems(it)
        }

    }

    companion object {
        private const val MATCHES_KEY = "matches"

        fun newInstance(matches: ArrayList<Match>): ZappingDayFragment {
            val fragment = ZappingDayFragment()
            val bundle = Bundle()
            bundle.putSerializable(MATCHES_KEY, matches)
            fragment.arguments = bundle
            return fragment
        }
    }
}
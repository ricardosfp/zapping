package com.ricardosfp.zapping.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ricardosfp.zapping.databinding.FragmentZappingDayBinding
import com.ricardosfp.zapping.domain.model.Match
import com.ricardosfp.zapping.ui.adapter.ZappingDayAdapter

class ZappingDayFragment: Fragment() {
    private lateinit var viewBinding: FragmentZappingDayBinding
    private lateinit var adapter: ZappingDayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        viewBinding = FragmentZappingDayBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @SuppressWarnings("unchecked")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ZappingDayAdapter()
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false)

        // how to solve this Unchecked Cast ?
        // todo this should just be passed to the adapter's constructor
        (arguments?.getSerializable(MATCHES_KEY) as? List<Match>)?.also {
            adapter.setItems(it)
        }

    }

    companion object {
        private const val MATCHES_KEY = "matches"

        fun newInstance(matches: List<Match>): ZappingDayFragment {
            val fragment = ZappingDayFragment()
            val bundle = Bundle()
            bundle.putSerializable(MATCHES_KEY, ArrayList(matches))
            fragment.arguments = bundle
            return fragment
        }
    }
}
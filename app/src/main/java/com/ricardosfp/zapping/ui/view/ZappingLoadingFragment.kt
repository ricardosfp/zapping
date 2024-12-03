package com.ricardosfp.zapping.ui.view

import android.os.*
import android.view.*
import androidx.fragment.app.*
import com.ricardosfp.zapping.*

class ZappingLoadingFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_zapping_loading, container, false)
    }

}
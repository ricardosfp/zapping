package com.ricardosfp.zapping.ui.view

import android.os.*
import android.view.*
import androidx.appcompat.app.*
import com.ricardosfp.zapping.R
import com.ricardosfp.zapping.databinding.*
import dagger.hilt.android.*

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(viewBinding.fragmentFrame.id, ZappingFragment::class.java, null)
                .commit()
        }

        setContentView(viewBinding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }
}
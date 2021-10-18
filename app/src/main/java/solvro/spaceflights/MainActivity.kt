package solvro.spaceflights

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog

import androidx.core.content.ContextCompat

import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import solvro.spaceflights.adapters.PagerAdapter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = arrayListOf(getString(R.string.all), getString(R.string.favourite))

        val pager = findViewById<ViewPager2>(R.id.pager)
        pager.adapter = PagerAdapter(supportFragmentManager, lifecycle, list)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = list[position]
        }.attach()

    }
}
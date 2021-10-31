package solvro.spaceflights.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.content.res.ResourcesCompat

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import solvro.spaceflights.R
import solvro.spaceflights.adapters.PagerAdapter
import solvro.spaceflights.fragments.AllArticlesFragment


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

        window.exitTransition = null
        window.enterTransition = null
        window.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.background, null
            )
        )
    }

    override fun onBackPressed() {
        val searchView = findViewById<SearchView>(R.id.search_view)
        if (searchView?.visibility == View.VISIBLE) {
            searchView.visibility = View.GONE
            val fragments = supportFragmentManager.fragments
            for (fragment in fragments) {
                if (fragment is AllArticlesFragment)
                    fragment.loadAllArticles()
            }
        } else {
            finish()
        }
    }
}
package solvro.spaceflights.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import solvro.spaceflights.fragments.AllArticlesFragment
import solvro.spaceflights.fragments.FavArticlesFragment

class PagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val data: ArrayList<String>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (data[position] == "all") {
            AllArticlesFragment()
        } else {
            FavArticlesFragment()
        }
    }
}
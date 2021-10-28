package solvro.spaceflights.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import solvro.spaceflights.R
import solvro.spaceflights.adapters.RecyclerAdapter
import solvro.spaceflights.database.AppDatabase

class FavArticlesFragment : ArticlesFragment() {
    private lateinit var adapter: RecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getArticlesFromDatabase()

        val swipeRefresh =
            requireActivity().findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_favourite)
        swipeRefresh.setOnRefreshListener {
            getArticlesFromDatabase()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun getArticlesFromDatabase() {
        val db = AppDatabase.invoke(requireActivity())

        GlobalScope.launch {
            list = db.dao().getFavourite().toMutableList()

            requireActivity().runOnUiThread {
                val recyclerView = requireView().findViewById<RecyclerView>(R.id.recycler_view)
                adapter = RecyclerAdapter(
                    list,
                    requireActivity(),
                    this@FavArticlesFragment,
                )
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getArticlesFromDatabase()
    }

    override fun dataSetChanged(isFavourite: Boolean, position: Int) {
        if (!isFavourite) {
            list!!.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }
}
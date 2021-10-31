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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode

import org.greenrobot.eventbus.Subscribe
import solvro.spaceflights.MessageEvent


class FavArticlesFragment : ArticlesFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        EventBus.getDefault().register(this)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        if (event.direction) {
            if (event.entity == null) {
                for (i in 0..list!!.lastIndex) {
                    if (list!![i].id == event.id) {
                        list!!.removeAt(i)
                        adapter?.notifyItemRemoved(i)
                        return
                    }
                }
            } else {
                for (i in 0..list!!.lastIndex) {
                    if (list!![i].updatedAt!! < event.entity.updatedAt!!) {
                        list!!.add(i, event.entity)
                        adapter?.notifyItemInserted(i)
                        return
                    }
                }
                list!!.add(event.entity)
                adapter?.notifyItemInserted(list!!.lastIndex)
            }
        }
    }

    override fun onDataSetChanged(isFavourite: Boolean, position: Int) {
        EventBus.getDefault().post(MessageEvent(list!![position].id, null, false))
        list!!.removeAt(position)
        adapter?.notifyItemRemoved(position)
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
}
package solvro.spaceflights.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import solvro.spaceflights.R
import solvro.spaceflights.api.Article

class FavouriteArticlesFragment : AbstractArticlesFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getArticles()

        val swipeRefresh =
            requireActivity().findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_favourite)
        swipeRefresh.setOnRefreshListener {
            getArticles()
            swipeRefresh.isRefreshing = false
        }
    }

    override fun formatList(list: List<Article>?): List<Article> {
        val favourites = ArrayList<Article>()

        for (element in list!!) {
            if (requireActivity().getSharedPreferences("favourites", Context.MODE_PRIVATE)
                    .getString(element.id.toString(), "N") == "Y"
            ) {
                favourites.add(element)
            }
        }

        return favourites
    }

    override fun stopShimmer() {

    }
}
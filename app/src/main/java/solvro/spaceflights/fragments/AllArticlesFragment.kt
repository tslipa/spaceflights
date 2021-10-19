package solvro.spaceflights.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import solvro.spaceflights.R
import solvro.spaceflights.api.Article


class AllArticlesFragment : AbstractArticlesFragment() {
    private val sortType = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getArticles()

        val swipeRefresh =
            requireActivity().findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_all)
        swipeRefresh.setOnRefreshListener {
            getArticles()
            swipeRefresh.isRefreshing = false
        }

        setFABListeners()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<ShimmerFrameLayout>(R.id.shimmer_all)!!
            .startShimmerAnimation()
    }

    private fun setFABListeners() {
        val fabSearch = requireActivity().findViewById<FloatingActionButton>(R.id.fab_search)
        val fabSort = requireActivity().findViewById<FloatingActionButton>(R.id.fab_sort)

        requireActivity().findViewById<RecyclerView>(R.id.recycler_view)
            .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0 && fabSearch.isShown) {
                        fabSearch.hide()
                    }
                    if (dy > 0 || dy < 0 && fabSort.isShown) {
                        fabSort.hide()
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        fabSearch.show()
                        fabSort.show()
                    }
                }
            })

        fabSort.setOnClickListener {
            val choices = resources.getStringArray(R.array.sort_possibilities)

            var newSortType: Int = sortType

            val dialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.choose_sort))
                .setSingleChoiceItems(choices, sortType) { _, which -> newSortType = which }
                .setPositiveButton(getString(R.string.ok)) { _, _ -> doSort(newSortType) }
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
            dialog.show()
        }
    }

    override fun formatList(list: List<Article>?): List<Article> {
        return list!!
    }

    override fun stopShimmer() {
        requireActivity().findViewById<ShimmerFrameLayout>(R.id.shimmer_all)!!
            .stopShimmerAnimation()
        requireActivity().findViewById<ShimmerFrameLayout>(R.id.shimmer_all)!!.visibility =
            View.GONE

        //TODO żeby nie powtarzać
    }

    private fun doSort(type: Int) {

    }
}
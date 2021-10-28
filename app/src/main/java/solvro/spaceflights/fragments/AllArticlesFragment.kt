package solvro.spaceflights.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import solvro.spaceflights.R
import solvro.spaceflights.api.Article

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import solvro.spaceflights.adapters.RecyclerAdapter
import solvro.spaceflights.api.RetrofitClientInstance
import solvro.spaceflights.api.RetrofitDataGetter
import solvro.spaceflights.database.AppDatabase
import solvro.spaceflights.database.DatabaseUtils
import org.greenrobot.eventbus.ThreadMode

import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus
import solvro.spaceflights.MessageEvent

@SuppressLint("NotifyDataSetChanged")
class AllArticlesFragment : ArticlesFragment() {
    private var sortType = 0
    private var adapter: RecyclerAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: AppDatabase
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        EventBus.getDefault().register(this)
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)!!
        sortType = sharedPreferences.getInt("sort", 0)

        searchView = requireActivity().findViewById(R.id.search_view)
        db = AppDatabase.invoke(requireActivity())

        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recycler_view)
        adapter = RecyclerAdapter(
            ArrayList(),
            requireActivity(),
            this@AllArticlesFragment,
        )
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        loadAllArticles()
        reloadArticlesFromAPI()

        val swipeRefresh =
            requireActivity().findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_all)
        swipeRefresh.setOnRefreshListener {
            reloadArticlesFromAPI()
            swipeRefresh.isRefreshing = false
        }

        setFABListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        if (!event.direction) {
            for (i in 0 until list!!.lastIndex) {
                if (list!![i].id == event.id) {
                    list!![i].favourite = false
                    adapter?.notifyItemChanged(i)
                }
            }
        }
    }

    override fun onDataSetChanged(isFavourite: Boolean, position: Int) {
        if (!isFavourite) {
            EventBus.getDefault().post(MessageEvent(list!![position].id, null, true))
        } else {
            EventBus.getDefault().post(MessageEvent(list!![position].id, list!![position], true))
        }
    }

    fun loadAllArticles() {
        GlobalScope.launch {
            list = db.dao().getAll().toMutableList()

            requireActivity().runOnUiThread {
                adapter?.dataSet = list
                adapter?.notifyDataSetChanged()

                sort()
            }
        }
    }

    private fun reloadArticlesFromAPI() {
        val service = RetrofitClientInstance.retrofitInstance!!.create(
            RetrofitDataGetter::class.java
        )

        val call: Call<List<Article>?>? = service.articles
        call!!.enqueue(object : Callback<List<Article>?> {
            override fun onResponse(
                call: Call<List<Article>?>,
                response: Response<List<Article>?>
            ) {
                val articles = (response.body())!!

                GlobalScope.launch {
                    for (element in articles) {
                        val entity = DatabaseUtils.toEntity(element, false)
                        db.dao().addEntity(entity)
                    }
                    requireActivity().runOnUiThread {
                        loadAllArticles()
                    }
                }
            }

            override fun onFailure(call: Call<List<Article>?>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Unable to download articles. Make sure that you have internet connection.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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

            val dialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.choose_sort))
                .setSingleChoiceItems(choices, sortType) { _, which -> sortType = which }
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    sharedPreferences.edit().putInt("sort", sortType).apply()
                    sort()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
            dialog.show()
        }

        fabSearch.setOnClickListener {
            if (searchView.visibility == View.GONE) {
                search()
            } else {
                submitQuery(searchView.query.toString())
            }
        }
    }

    private fun sort() {
        list!!.sortWith { lhs, rhs ->
            when (sortType) {
                0 -> if (lhs.updatedAt!! > rhs.updatedAt!!) -1 else if (lhs.updatedAt < rhs.updatedAt) 1 else 0
                1 -> if (lhs.publishedAt!! > rhs.publishedAt!!) -1 else if (lhs.publishedAt < rhs.publishedAt) 1 else 0
                else -> if (lhs.title!! > rhs.title!!) 1 else if (lhs.title < rhs.title) -1 else 0
            }
        }

        adapter?.notifyDataSetChanged()
    }

    private fun search() {
        searchView.visibility = View.VISIBLE

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    submitQuery(query!!)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = true
            })

        searchView.setOnCloseListener {
            searchView.visibility = View.GONE
            loadAllArticles()
            true
        }

        searchView.setOnClickListener { searchView.isIconified = false }
    }

    fun submitQuery(query: String) {
        if (requireActivity().currentFocus != null) {
            val imm: InputMethodManager? =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        GlobalScope.launch {
            list = db.dao().getFromQuery("%$query%").toMutableList()
            requireActivity().runOnUiThread {
                adapter?.dataSet = list
                adapter?.notifyDataSetChanged()
                sort()
            }
        }
    }
}
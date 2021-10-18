package solvro.spaceflights

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import solvro.spaceflights.api.RetrofitClientInstance.retrofitInstance
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Callback
import retrofit2.Response
import solvro.spaceflights.adapters.RecyclerAdapter
import solvro.spaceflights.api.Article
import solvro.spaceflights.api.RetrofitDataGetter


class FragmentAll : Fragment() {
    private var list: List<Article>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getArticles()

        val swipeRefresh = requireActivity().findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        swipeRefresh.setOnRefreshListener {
            getArticles()
            swipeRefresh.isRefreshing = false
        }
    }

    fun startActivityArticle(tag: Int) {
        if (list != null) {
            val intent = Intent(activity, ActivityArticle::class.java)
            intent.putExtra("id", tag)
            startActivity(intent)
        }
    }

    private fun getArticles() {
        val service = retrofitInstance!!.create(
            RetrofitDataGetter::class.java
        )

        val call: Call<List<Article>?>? = service.articles
        call!!.enqueue(object : Callback<List<Article>?> {
            override fun onResponse(
                call: Call<List<Article>?>,
                response: Response<List<Article>?>
            ) {
                list = response.body()
                val recyclerView = view!!.findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = RecyclerAdapter(list, activity!!, this@FragmentAll)
            }

            override fun onFailure(call: Call<List<Article>?>, t: Throwable) {
                Toast.makeText(
                    activity,
                    t.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
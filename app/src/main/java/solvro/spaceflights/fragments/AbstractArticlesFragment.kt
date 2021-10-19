package solvro.spaceflights.fragments

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import solvro.spaceflights.ArticleActivity
import solvro.spaceflights.R
import solvro.spaceflights.adapters.RecyclerAdapter
import solvro.spaceflights.api.Article
import solvro.spaceflights.api.RetrofitClientInstance
import solvro.spaceflights.api.RetrofitDataGetter

abstract class AbstractArticlesFragment : Fragment() {
    var list: List<Article>? = null

    fun getArticles() {
        val service = RetrofitClientInstance.retrofitInstance!!.create(
            RetrofitDataGetter::class.java
        )

        val call: Call<List<Article>?>? = service.articles
        call!!.enqueue(object : Callback<List<Article>?> {
            override fun onResponse(
                call: Call<List<Article>?>,
                response: Response<List<Article>?>
            ) {
                list = formatList(response.body())
                val recyclerView = view!!.findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter =
                    RecyclerAdapter(list, activity!!, this@AbstractArticlesFragment)

                stopShimmer()
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

    abstract fun formatList(list: List<Article>?): List<Article>

    abstract fun stopShimmer()

    fun startActivityArticle(tag: Int) {
        if (list != null) {
            val intent = Intent(activity, ArticleActivity::class.java)
            intent.putExtra("id", tag)
            startActivity(intent)
        }
    }

}

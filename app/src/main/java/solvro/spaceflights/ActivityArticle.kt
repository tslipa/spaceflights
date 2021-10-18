package solvro.spaceflights

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import solvro.spaceflights.api.Article
import solvro.spaceflights.api.RetrofitClientInstance
import solvro.spaceflights.api.RetrofitDataGetter

class ActivityArticle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val service = RetrofitClientInstance.retrofitInstance!!.create(
            RetrofitDataGetter::class.java
        )

        val call: Call<Article> = service.getArticle(intent.getIntExtra("id", 0).toString())
        call.enqueue(object: Callback<Article> {
            override fun onResponse(
                call: Call<Article>,
                response: Response<Article>
            ) {
                response.body()?.let { displayArticle(it) }
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                Toast.makeText(
                    this@ActivityArticle,
                    t.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun displayArticle(article: Article) {
        findViewById<TextView>(R.id.text_article_date).text = article.publishedAt
        findViewById<TextView>(R.id.text_article_title).text = article.title
        findViewById<TextView>(R.id.text_article_summary).text = article.summary
    }
}
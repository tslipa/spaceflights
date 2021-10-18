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

import android.graphics.drawable.Drawable

import android.widget.ImageView
import com.bumptech.glide.Glide

import android.view.View
import android.widget.ScrollView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class ActivityArticle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val service = RetrofitClientInstance.retrofitInstance!!.create(
            RetrofitDataGetter::class.java
        )

        val call: Call<Article> = service.getArticle(intent.getIntExtra("id", 0).toString())
        call.enqueue(object : Callback<Article> {
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
        val imageView = findViewById<View>(R.id.imageView) as ImageView

        Glide.with(this)
            .load(article.imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@ActivityArticle,
                            R.drawable.rocket
                        )
                    )
                    displayTexts(article)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    displayTexts(article)
                    return false
                }
            })
            .into(imageView)
    }

    private fun displayTexts(article: Article) {
        findViewById<TextView>(R.id.text_article_published).text = getString(
            R.string.published,
            article.publishedAt?.subSequence(0, 10),
            article.publishedAt?.subSequence(11, 19)
        )
        findViewById<TextView>(R.id.text_article_updated).text = getString(
            R.string.updated,
            article.updatedAt?.subSequence(0, 10),
            article.updatedAt?.subSequence(11, 19)
        )
        findViewById<TextView>(R.id.text_article_title).text = article.title
        findViewById<TextView>(R.id.text_article_summary).text = article.summary
        findViewById<TextView>(R.id.text_article_news_site).text = article.newsSite

        findViewById<ScrollView>(R.id.scroll_view).visibility = View.VISIBLE
    }
}
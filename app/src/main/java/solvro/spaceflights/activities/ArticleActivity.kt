package solvro.spaceflights.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import solvro.spaceflights.api.Article

import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod

import android.widget.ImageView
import com.bumptech.glide.Glide

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import solvro.spaceflights.R
import solvro.spaceflights.database.AppDatabase
import solvro.spaceflights.database.DatabaseUtils


class ArticleActivity : AppCompatActivity() {
    private lateinit var shimmer: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        shimmer = findViewById(R.id.shimmer)!!
        shimmer.startShimmerAnimation()

        window.exitTransition = null
        window.enterTransition = null

        val db = AppDatabase.invoke(this)

        GlobalScope.launch {
            val entity = db.dao().getEntity(intent.getIntExtra("id", 0))
            val article = DatabaseUtils.toArticle(entity[0])

            runOnUiThread {
                displayArticle(article)
            }
        }
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
                            this@ArticleActivity,
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
        shimmer.stopShimmerAnimation()
        shimmer.visibility = View.GONE

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

        val textSource = findViewById<TextView>(R.id.text_article_news_site)
        textSource.text = getString(R.string.source, article.url, article.newsSite)
        textSource.movementMethod = LinkMovementMethod.getInstance()

        findViewById<CardView>(R.id.card_article).visibility = View.VISIBLE
    }
}
package solvro.spaceflights.fragments

import android.content.Intent
import androidx.fragment.app.Fragment
import solvro.spaceflights.activities.ArticleActivity
import android.app.ActivityOptions
import org.greenrobot.eventbus.EventBus
import solvro.spaceflights.adapters.RecyclerAdapter
import solvro.spaceflights.database.Entity

abstract class ArticlesFragment : Fragment() {
    var list: MutableList<Entity>? = null
    var adapter: RecyclerAdapter? = null

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    abstract fun onDataSetChanged(isFavourite: Boolean, position: Int)

    fun startActivityArticle(tag: Int) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra("id", tag)
        val bundle = ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
        startActivity(intent, bundle)
    }
}

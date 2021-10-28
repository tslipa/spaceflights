package solvro.spaceflights.fragments

import android.content.Intent
import androidx.fragment.app.Fragment
import solvro.spaceflights.ArticleActivity
import android.app.ActivityOptions
import solvro.spaceflights.database.Entity

abstract class ArticlesFragment : Fragment() {
    var list: MutableList<Entity>? = null

    abstract fun onDataSetChanged(isFavourite: Boolean, position: Int)

    fun startActivityArticle(tag: Int) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra("id", tag)
        val bundle = ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
        startActivity(intent, bundle)
    }

}

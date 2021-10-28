package solvro.spaceflights.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import solvro.spaceflights.fragments.ArticlesFragment
import solvro.spaceflights.R
import solvro.spaceflights.database.AppDatabase
import solvro.spaceflights.database.Entity


class RecyclerAdapter(
    var dataSet: List<Entity>?,
    private val mContext: Context,
    private val fragment: ArticlesFragment,
) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        private val view: View,
        private val fragment: ArticlesFragment,
        private val context: Context,
        private val adapter: RecyclerAdapter
    ) :
        RecyclerView.ViewHolder(view) {
        val textDate: TextView = view.findViewById(R.id.text_date)
        val textTitle: TextView = view.findViewById(R.id.text_title)
        val textSummary: TextView = view.findViewById(R.id.text_summary)
        private val imageIsFavourite: ImageView = view.findViewById(R.id.image_isfavourite)

        init {
            view.setOnClickListener {
                fragment.startActivityArticle(view.tag as Int)
            }

            imageIsFavourite.setOnClickListener {
                val db = AppDatabase.invoke(context)

                val favourite = adapter.dataSet?.get(adapterPosition)!!.favourite
                adapter.dataSet?.get(adapterPosition)!!.favourite = !favourite
                setImageIsFavourite()
                fragment.dataSetChanged(!favourite, adapterPosition)
                GlobalScope.launch {
                    db.dao().setIfFavourite(view.tag as Int, !favourite)
                }
            }
        }

        fun setImageIsFavourite() {
            val resource = if (adapter.dataSet?.get(adapterPosition)!!.favourite) {
                R.drawable.star2
            } else {
                R.drawable.star1
            }

            imageIsFavourite.setImageDrawable(
                ResourcesCompat.getDrawable(
                    context.resources,
                    resource,
                    null
                )
            )
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_item, viewGroup, false)
        return ViewHolder(view, fragment, mContext, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = dataSet!![position]
        holder.itemView.tag = article.id
        holder.setImageIsFavourite()
        holder.textDate.text = mContext.getString(
            R.string.date_format,
            article.updatedAt?.subSequence(0, 10),
            article.updatedAt?.subSequence(11, 19)
        )
        holder.textTitle.text = article.title
        val summary = article.summary!!
        holder.textSummary.text = if (summary.length <= 75) {
            summary
        } else {
            StringBuilder().append(summary.substring(0, 75)).append("...").toString()
        }
    }

    override fun getItemCount(): Int {
        return dataSet!!.size
    }
}
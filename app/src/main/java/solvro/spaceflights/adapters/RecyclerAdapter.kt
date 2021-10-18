package solvro.spaceflights.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import solvro.spaceflights.FragmentAll
import solvro.spaceflights.R
import solvro.spaceflights.api.Article


class RecyclerAdapter(
    private val dataSet: List<Article>?,
    private val mContext: Context,
    private val fragment: FragmentAll
) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val fragment: FragmentAll) :
        RecyclerView.ViewHolder(view) {
        val textDate: TextView = view.findViewById(R.id.text_date)
        val textTitle: TextView = view.findViewById(R.id.text_title)
        val textSummary: TextView = view.findViewById(R.id.text_summary)

        init {
            view.setOnClickListener {
                fragment.startActivityArticle(view.tag as Int)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_item, viewGroup, false)
        return ViewHolder(view, fragment)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = dataSet!![position]
        holder.itemView.tag = position
        holder.textDate.text = article.publishedAt
        holder.textTitle.text = article.title
        holder.textSummary.text =
            StringBuilder().append(article.summary!!.substring(0, 75)).append("...").toString()
    }

    override fun getItemCount(): Int {
        return dataSet!!.size
    }
}